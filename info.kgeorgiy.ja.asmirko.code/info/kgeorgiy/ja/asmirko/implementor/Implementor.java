package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

public class Implementor implements Impler, JarImpler {

    public static void main(String[] args) {
        if (args.length < 2 || args[0] == null || args[1] == null) {
            System.out.println("Implementor should take two strings");
            return;
        }
        Path resultFile = Path.of(args[1]);
        Class<?> token;
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Implementor implementor = new Implementor();
        try {
            token = classLoader.loadClass(args[0]);
            implementor.implementJar(token, resultFile);
        } catch (ClassNotFoundException | ImplerException e) {
            e.printStackTrace();
        }
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
                constructorsCode(token),
                methodsCode(token));

        writeResult(token, root, result);
    }

    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (var jOS = new JarOutputStream(Files.newOutputStream(jarFile))) {
            String implementedPath = Path
                    .of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            if (compiler.run(null, null, null, "-cp", implementedPath,
                    getPathToTargetFile(jarFile.getParent(), token, "java").toString()) != 0) {
                throw new ImplerException("Can't compile generated class");
            }
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            jOS.putNextEntry(new ZipEntry(JarFile.MANIFEST_NAME));
            manifest.write(jOS);
            ZipEntry jar = new ZipEntry(getPathToTargetFile(jarFile.getParent(), token, "class").toString());
            jOS.putNextEntry(jar);
            Files.copy(getPathToTargetFile(jarFile.getParent(), token, "class"), jOS);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new ImplerException(e.getMessage());
        } finally {
//            try {
//                CustomSimpleFileVisitor visitor = new CustomSimpleFileVisitor(jarFile, jarFile.getParent());
//                Files.walkFileTree(jarFile.getParent(), visitor);
//            } catch (IOException e) {
//                e.printStackTrace();
//                throw new ImplerException(e.getMessage());
//            }
        }
    }

    private Path getPathToTargetFile(Path jarFile, Class<?> token, String fileExtension) {
        return Paths.get(jarFile.toString(), token.getPackageName().replace(".", File.separator),
                token.getSimpleName() + "Impl." + fileExtension);
    }

    private void writeResult(Class<?> token, Path root, String result) throws ImplerException {
        Path pathToFile = root.resolve(token.getPackageName().replace(".", File.separator));
        Path file = pathToFile.resolve(String.format("%sImpl.java", token.getSimpleName()));
        try {
            Files.createDirectories(pathToFile);
            if (Files.notExists(file)) {
                Files.createFile(file);
            }
            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                bw.write(result);
            }
        } catch (IOException e) {
            throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
        }
    }

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

    private String constructorsCode(Class<?> token) {
        return mapAndJoin(Arrays.stream(token.getDeclaredConstructors())
                        .filter(c -> !Modifier.isPrivate(c.getModifiers())),
                m -> mm(m, c -> c.getDeclaringClass().getSimpleName(),
                        "public %s%sImpl(%s) %s {%n super(%s);%n}%n",
                        streamMapAndJoin(m.getParameters(), Parameter::getName)));
    }

    private String methodsCode(Class<?> token) {
        return mapAndJoin(getAllMethods(token)
                        .map(MethodWrapper::getMethod),
                m -> mm(m, Method::getName,
                        "public %s %s(%s) %s {%n return %s;%n}%n",
                        getDefaultValue(m.getReturnType()),
                        Modifier.isStatic(m.getModifiers()) ? "static " : "",
                        m.getReturnType().getCanonicalName()));
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

    private static class CustomSimpleFileVisitor extends SimpleFileVisitor<Path> {

        final private HashSet<Path> excludePaths;

        public CustomSimpleFileVisitor(Path... excludePaths) {
            this.excludePaths = new HashSet<>(Arrays.asList(excludePaths));
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
            deleteIfAbsent(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
            deleteIfAbsent(dir);
            return FileVisitResult.CONTINUE;
        }

        private void deleteIfAbsent(Path path) throws IOException {
            if (!excludePaths.contains(path)) {
                Files.delete(path);
            }
        }
    }
}