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

    private String joinExceptions(Class<?>[] exceptions) {
        return exceptions.length > 0 ?
                String.format("throws %s", Arrays.stream(exceptions)
                        .map(Class::getCanonicalName)
                        .collect(Collectors.joining(", ")))
                : "";
    }

    private String joinArguments(Class<?>[] arguments) {
        return IntStream.range(0, arguments.length)
                .mapToObj(i -> String.format("%s arg%d", arguments[i].getCanonicalName(), i))
                .collect(Collectors.joining(", "));
    }

    private String getDefaultValue(Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return "null";
        } else if (clazz == boolean.class) {
            return "false";
        } else if (clazz == void.class) {
            return "";
        } else {
            return "0";
        }
    }

    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        if (token.isPrimitive() || token.isEnum() || token == Enum.class) {
            throw new ImplerException("token should be interface or class");
        }
        final int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ImplerException("Can't implement private interface or extend private or final class");
        }
        final StringBuilder classCode = new StringBuilder();
        classCode.append(String.format("%s %n public class %sImpl %s %s {%n%n",
                token.getPackageName().equals("") ? "" : String.format("package %s;", token.getPackageName()),
                token.getSimpleName(),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName())
        );

        Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (constructors.length != 0
                && Arrays.stream(constructors).allMatch(c -> Modifier.isPrivate(c.getModifiers()))) {
            throw new ImplerException("YO");
        }
        for (Constructor<?> c : constructors) {
            final int cModifiers = c.getModifiers();
            if (Modifier.isPrivate(cModifiers))
                continue;
            final Class<?>[] mParameterTypes = c.getParameterTypes();
            final Class<?>[] mExceptions = c.getExceptionTypes();
            classCode.append(String.format("public %sImpl(%s) %s {%n    super(%s);%n    }%n",
                    token.getSimpleName(),
                    joinArguments(mParameterTypes),
                    joinExceptions(mExceptions),
                    IntStream.range(0, mParameterTypes.length)
                            .mapToObj(i -> String.format("arg%d", i))
                            .collect(Collectors.joining(", "))));
        }

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

            final Class<?>[] mParameterTypes = m.getParameterTypes();
            final Class<?>[] mExceptions = m.getExceptionTypes();
            classCode.append(String.format("public %s %s %s(%s) %s {%n    return %s;%n    }%n",
                    Modifier.isStatic(mModifiers) ? "static " : "",
                    returnType.getCanonicalName(),
                    m.getName(),
                    joinArguments(mParameterTypes),
                    joinExceptions(mExceptions),
                    getDefaultValue(returnType)));
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
            argTypes = Arrays.stream(m.getParameterTypes()).map(Class::getCanonicalName).collect(Collectors.toList());
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