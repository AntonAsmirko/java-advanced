package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Implementor implements Impler {

    private List<MethodWrapper> getAllMethods(Class<?> token) {
        LinkedList<Class<?>> queue = new LinkedList<>();
        HashSet<MethodWrapper> processedMethods = new HashSet<>();
        queue.add(token);
        while (!queue.isEmpty()) {
            Class<?> cur = queue.removeFirst();
            for (final Method method : cur.getDeclaredMethods()) {
                final int mModifiers = method.getModifiers();
                final Class<?> returnType = method.getReturnType();
                MethodWrapper mw = new MethodWrapper(method);
                if (Modifier.isPrivate(mModifiers) || processedMethods.contains(mw)
                        || returnType.getCanonicalName().contains("internal")) {
                    continue;
                }
                processedMethods.add(mw);
            }
            if (cur.getSuperclass() != null) {
                queue.add(cur.getSuperclass());
            }
            queue.addAll(Arrays.asList(cur.getInterfaces()));
        }
        return processedMethods.stream()
                .filter(m -> !Modifier.isFinal(m.method.getModifiers()))
                .collect(Collectors.toList());
    }

    private <T> String streamMapAndJoin(T[] arr, Function<T, String> fn) {
        return Arrays.stream(arr).map(fn).collect(Collectors.joining(", "));
    }

    private String joinExceptions(Class<?>[] exceptions) {
        return exceptions.length > 0 ?
                String.format("throws %s", streamMapAndJoin(exceptions, Class::getCanonicalName)) : "";
    }

    private String joinArguments(Parameter[] arguments) {
        return streamMapAndJoin(arguments, i -> String.format("%s %s", i.getType().getCanonicalName(), i.getName()));
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
        final int modifiers = token.getModifiers();
        Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (token.isPrimitive() || token.isEnum() ||
                token == Enum.class || Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ImplerException("can't implement given token");
        }
        if (constructors.length != 0
                && Arrays.stream(constructors).allMatch(c -> Modifier.isPrivate(c.getModifiers()))) {
            throw new ImplerException("YO");
        }
        final StringBuilder classCode = new StringBuilder();
        classCode.append(String.format("%s %n public class %sImpl %s %s {%n%n",
                token.getPackageName().equals("") ? "" : String.format("package %s;", token.getPackageName()),
                token.getSimpleName(),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName())
        );

        classCode.append(Arrays.stream(token.getDeclaredConstructors())
                .filter(c -> !Modifier.isPrivate(c.getModifiers()))
                .map(c -> String.format("public %sImpl(%s) %s {%n    super(%s);%n    }%n",
                        token.getSimpleName(),
                        joinArguments(c.getParameters()),
                        joinExceptions(c.getExceptionTypes()),
                        Arrays.stream(c.getParameters())
                                .map(Parameter::getName).collect(Collectors.joining(", "))))
                .collect(Collectors.joining()));

        for (final MethodWrapper mWrapper : getAllMethods(token)) {
            final Method method = mWrapper.method;
            final Class<?> returnType = method.getReturnType();
            classCode.append(String.format("public %s %s %s(%s) %s {%n    return %s;%n    }%n",
                    Modifier.isStatic(method.getModifiers()) ? "static " : "",
                    returnType.getCanonicalName(),
                    method.getName(),
                    joinArguments(method.getParameters()),
                    joinExceptions(method.getExceptionTypes()),
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
        final Method method;

        public MethodWrapper(Method m) {
            this.argTypes = Arrays.stream(m.getParameterTypes()).map(Class::getCanonicalName).collect(Collectors.toList());
            this.name = m.getName();
            this.arsHash = argTypes.hashCode();
            this.method = m;
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