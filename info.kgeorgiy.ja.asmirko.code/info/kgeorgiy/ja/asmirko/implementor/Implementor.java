package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Implementor implements Impler {

    private ArrayList<Method> getAllMethods(Class<?> token) {
        ArrayList<Method> methods = new ArrayList<>();
        LinkedList<Class<?>> queue = new LinkedList<>();
        queue.add(token);
        while (!queue.isEmpty()) {
            Class<?> cur = queue.removeFirst();
            methods.addAll(List.of(cur.getDeclaredMethods()));
            if (cur.getSuperclass() != null) {
                queue.add(cur.getSuperclass());
            }
            queue.addAll(Arrays.asList(cur.getInterfaces()));
        }
        return methods;
    }


    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        if (token.isPrimitive() || token.isEnum() || token.getCanonicalName().equals("java.lang.Enum")) {
            throw new ImplerException("token should be interface or class");
        }
        final int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ImplerException("Can't implement private interface or extend private or final class");
        }
        final StringBuilder classCode = new StringBuilder();
        classCode.append(String.format("package %s; %n", token.getPackageName()));
        if (Modifier.isPublic(modifiers)) {
            classCode.append("public ");
        }

        classCode.append(String.format("class %sImpl %s %s {%n%n",
                token.getSimpleName(),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName()));

        boolean allPrivate = true;
        Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (constructors.length == 0) {
            allPrivate = false;
        }
        for (Constructor<?> c : constructors) {
            final int cModifiers = c.getModifiers();
            classCode.append("    ");

            if (Modifier.isPrivate(cModifiers))
                continue;
            allPrivate = false;
            if (Modifier.isPublic(cModifiers))
                classCode.append("public ");
            if (Modifier.isProtected(cModifiers))
                classCode.append("protected ");
            final Class<?>[] mParameterTypes = c.getParameterTypes();
            final Class<?>[] mExceptions = c.getExceptionTypes();
            classCode.append(String.format("%sImpl(%s) %s {%n    super(%s)",
                    token.getSimpleName(),
                    IntStream.range(0, mParameterTypes.length)
                            .mapToObj(i -> String.format("%s arg%d", mParameterTypes[i].getCanonicalName(), i))
                            .collect(Collectors.joining(", ")),
                    mExceptions.length > 0 ?
                            String.format("throws %s", Arrays.stream(mExceptions)
                                    .map(Class::getCanonicalName)
                                    .collect(Collectors.joining(", ")))
                            : "",
                    IntStream.range(0, mParameterTypes.length)
                            .mapToObj(i -> String.format("arg%d", i))
                            .collect(Collectors.joining(", "))));
            classCode.append(String.format(";%n    }%n"));
        }

        if (allPrivate)
            throw new ImplerException("YO");

        HashSet<MethodWrapper> processedMethods = new HashSet<>();
        HashSet<MethodWrapper> finalMethods = new HashSet<>();
        for (Method m : getAllMethods(token)) {
            final int mModifiers = m.getModifiers();
            final Class<?> returnType = m.getReturnType();
            MethodWrapper mw = new MethodWrapper(m);
            if (Modifier.isPrivate(mModifiers) || processedMethods.contains(mw) || finalMethods.contains(mw)
                    || returnType.getCanonicalName().contains("internal")) {
                continue;
            }
            if (Modifier.isFinal(mModifiers)) {
                finalMethods.add(mw);
                continue;
            }
            processedMethods.add(mw);
            classCode.append("    ");
            if (Modifier.isPublic(mModifiers))
                classCode.append("public ");
            if (Modifier.isProtected(mModifiers))
                classCode.append("protected ");
            if (Modifier.isStatic(mModifiers))
                classCode.append("static ");

            final Class<?>[] mParameterTypes = m.getParameterTypes();
            final Class<?>[] mExceptions = m.getExceptionTypes();
            classCode.append(String.format("%s %s(%s) %s {%n    return",
                    returnType.getCanonicalName(),
                    m.getName(),
                    IntStream.range(0, mParameterTypes.length)
                            .mapToObj(i -> String.format("%s arg%d", mParameterTypes[i].getCanonicalName(), i))
                            .collect(Collectors.joining(", ")),
                    mExceptions.length > 0 ?
                            String.format("throws %s", Arrays.stream(mExceptions)
                                    .map(Class::getCanonicalName)
                                    .collect(Collectors.joining(", ")))
                            : ""));
            if (!returnType.getName().equals(void.class.getName())) {
                if (returnType.isPrimitive()) {
                    classCode.append(String.format(" %s", returnType.isAssignableFrom(boolean.class) ? "false" : "0"));
                } else {
                    classCode.append(" null");
                }
            }
            classCode.append(String.format(";%n    }%n"));
        }
        classCode.append("}");
        Path pathToFile = root.resolve(token.getPackageName().replace(".", "/"));
        Path file = pathToFile.resolve(String.format("%sImpl.java", token.getSimpleName()));
        try {
            Files.createDirectories(pathToFile);
            Files.createFile(file);
            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                bw.write(classCode.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
        }
    }

    private static class MethodWrapper {

        final List<String> argTypes;
        final String name;
        final int arsHash;

        public MethodWrapper(Method m) {
            argTypes = Arrays.stream(m.getParameterTypes())
                    .map(Class::getCanonicalName)
                    .sorted(Comparator.naturalOrder()).collect(Collectors.toUnmodifiableList());
            name = m.getName();
            arsHash = argTypes.hashCode();
        }

        @Override
        public int hashCode() {
            return arsHash * name.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof MethodWrapper) {
                MethodWrapper otherMethod = (MethodWrapper) other;
                return this.name.equals(otherMethod.name) && otherMethod.argTypes.equals(this.argTypes);
            }
            return false;
        }
    }
}