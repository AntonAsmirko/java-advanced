package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Implementor implements Impler {

    private Stream<MethodWrapper> getAllMethods(Class<?> token) {
        Stream<MethodWrapper> processedMethods = Arrays.stream(token.getMethods()).map(MethodWrapper::new);
        while (token != null) {
            processedMethods = Stream.concat(processedMethods,
                    Arrays.stream(token.getDeclaredMethods()).filter(m -> {
                        int modifiers = m.getModifiers();
                        return !Modifier.isPrivate(modifiers) && !Modifier.isPublic(modifiers);
                    }).map(MethodWrapper::new));
            token = token.getSuperclass();
        }
        return processedMethods.distinct().filter(m -> !Modifier.isFinal(m.method.getModifiers()))
                .filter(m -> !m.method.getReturnType().getCanonicalName().contains("internal"));
    }

    private <T extends Executable> String mm(T executable, Function<T, String> nameSup, String a, String b, String... c) {
        return String.format(a, String.join(" ", c),
                nameSup.apply(executable),
                joinArguments(executable.getParameters()),
                joinExceptions(executable.getExceptionTypes()), b);
    }


    private <T> String streamMapAndJoin(T[] arr, Function<T, String> fn) {
        return Arrays.stream(arr).map(fn).collect(Collectors.joining(", "));
    }

    private <T extends Executable> String mapAndJoin(Stream<T> stream, Function<T, String> fn) {
        return stream.map(fn).collect(Collectors.joining());
    }

    private String joinExceptions(Class<?>[] exceptions) {
        return exceptions.length > 0 ?
                String.format("throws %s", streamMapAndJoin(exceptions, Class::getCanonicalName)) : "";
    }

    private String joinArguments(Parameter[] arguments) {
        return streamMapAndJoin(arguments, i -> String.format("%s %s", i.getType().getCanonicalName(), i.getName()));
    }

    private String getDefaultValue(Class<?> clazz) {
        return clazz != void.class ? clazz == boolean.class ? "false" : !clazz.isPrimitive() ? "null" : "0" : "";
    }

    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        final int modifiers = token.getModifiers();
        final Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (token.isPrimitive() || token.isEnum() ||
                token == Enum.class || Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ImplerException("can't implement given token");
        }
        if (constructors.length != 0
                && Arrays.stream(constructors).allMatch(c -> Modifier.isPrivate(c.getModifiers()))) {
            throw new ImplerException("");
        }
        final String result = String.format("%s %n public class %sImpl %s %s {%n%n%s%n%s}",
                token.getPackageName().isEmpty() ? "" : String.format("package %s;", token.getPackageName()),
                token.getSimpleName(),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName(),
                mapAndJoin(Arrays.stream(token.getDeclaredConstructors())
                                .filter(c -> !Modifier.isPrivate(c.getModifiers())),
                        m -> mm(m, c -> c.getDeclaringClass().getSimpleName(),
                                "public %s%sImpl(%s) %s {%n super(%s);%n}%n",
                                streamMapAndJoin(m.getParameters(), Parameter::getName))),
                mapAndJoin(getAllMethods(token)
                                .map(MethodWrapper::getMethod),
                        m -> mm(m, Method::getName,
                                "public %s %s(%s) %s {%n return %s;%n}%n",
                                getDefaultValue(m.getReturnType()),
                                Modifier.isStatic(m.getModifiers()) ? "static " : "",
                                m.getReturnType().getCanonicalName())));

        Path pathToFile = root.resolve(token.getPackageName().replace(".", "/"));
        Path file = pathToFile.resolve(String.format("%sImpl.java", token.getSimpleName()));
        try {
            Files.createDirectories(pathToFile);
            Files.createFile(file);
            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                bw.write(result);
            }
        } catch (IOException e) {
            throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
        }
    }

    private static class MethodWrapper {

        final List<String> argTypes;
        final int arsHash;
        final Method method;

        public MethodWrapper(Method m) {
            this.argTypes = Arrays.stream(m.getParameterTypes())
                    .map(Class::getCanonicalName)
                    .collect(Collectors.toUnmodifiableList());
            this.arsHash = argTypes.hashCode();
            this.method = m;
        }

        @Override
        public int hashCode() {
            return arsHash * method.getName().hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof MethodWrapper) {
                if (this.hashCode() == other.hashCode())
                    return true;
                MethodWrapper otherMethod = (MethodWrapper) other;
                return this.method.getName().equals(otherMethod.method.getName())
                        && otherMethod.argTypes.equals(this.argTypes);
            }
            return false;
        }

        public Method getMethod() {
            return method;
        }
    }
}