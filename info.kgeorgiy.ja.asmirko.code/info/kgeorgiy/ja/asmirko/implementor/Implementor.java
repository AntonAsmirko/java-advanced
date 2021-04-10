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
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

/**
 * The purposes of this class are generation of new java classes which implement given type token and
 * creation of jar files with generated classes.
 *
 * @see Class
 * @see Impler
 * @see JarImpler
 */
public class Implementor implements Impler, JarImpler {

    /**
     * Generates jar file that contains generated class which implements given class or interface.
     *
     * @param args -jar class-name path to file.jar.
     */
    public static void main(String[] args) {
        if (args.length < 3 || args[1] == null || args[2] == null) {
            System.out.println("Implementor should take two strings");
            return;
        }
        Path resultFile = Path.of(args[2]);
        Class<?> token;
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Implementor implementor = new Implementor();
        try {
            token = classLoader.loadClass(args[1]);
            implementor.implement(token, resultFile.getParent());
            implementor.implementJar(token, resultFile);
        } catch (ClassNotFoundException | ImplerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates java class which is an implementation of given type token, implementation has suffix <code>Impl</code>.
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException if given type token is not class or interface or has not public
     *                         or <code>private</code> constructors.
     */
    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        final int modifiers = token.getModifiers();
        if (token.isPrimitive() || token.isEnum() ||
                token == Enum.class || Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers)) {
            throw new ImplerException("can't implement given token");
        }
        final Constructor<?>[] constructors = token.getDeclaredConstructors();
        if (constructors.length != 0
                && Arrays.stream(constructors).allMatch(c -> Modifier.isPrivate(c.getModifiers()))) {
            throw new ImplerException("implemented class does not have public constructors");

        }
        final String result = String.format("%s %n public class %sImpl %s %s {%n%n%s%n%s}",
                token.getPackageName().isEmpty() ? "" : String.format("package %s;", token.getPackageName()),
                token.getSimpleName(),
                token.isInterface() ? "implements" : "extends",
                token.getCanonicalName(),
                constructorsCode(constructors),
                methodsCode(token));

        writeResult(token, root, result);
    }

    /**
     * Generates <var>.jar</var> file with compiled class generated by {@link Implementor#implement(Class, Path)}
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException if <var>.java</var> file generated by {@link Implementor#implement(Class, Path)}
     *                         can't be compiled or token can't be added to class path while compilation.
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (var jOS = new JarOutputStream(Files.newOutputStream(jarFile))) {
            String implementedPath = Path
                    .of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            if (compiler.run(null, null, null, "-cp", implementedPath,
                    getPathToTargetFile(jarFile.getParent(), token, "java", File.separator).toString()) != 0) {
                throw new ImplerException("Can't compile generated class");
            }
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR, "Anton Asmirko");

            jOS.putNextEntry(new ZipEntry(JarFile.MANIFEST_NAME));
            manifest.write(jOS);
            ZipEntry jar = new ZipEntry(String.format("%s/%s", token.getPackageName().replace(".", "/"),
                    token.getSimpleName() + "Impl.class"));
            jOS.putNextEntry(jar);
            Files.copy(getPathToTargetFile(jarFile.getParent(), token, "class", "/"), jOS);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            throw new ImplerException(e.getMessage());
        }
    }

    /**
     * Generates path to target file used or generated by compiler in {@link Implementor#implementJar(Class, Path)}.
     *
     * @param root          prefix of the result {@link Path path}.
     * @param token         type token to get package.
     * @param fileExtension extension of desired file.
     * @return path to target file with specified extension.
     */
    private Path getPathToTargetFile(Path root, Class<?> token, String fileExtension, String separator) {
        return Path.of(String.format("%s%s%s%s%s", root.toString(),
                separator, token.getPackageName().replace(".", separator),
                separator, token.getSimpleName() + "Impl." + fileExtension));
    }

    /**
     * Writes code of class to <var>.java</var> file.
     *
     * @param token  type token to get package name and simple name.
     * @param root   prefix of path where result will be written.
     * @param result code of generated <var>.java</var> file.
     * @throws ImplerException if occurs errors with creation of file or writing to file.
     */
    private void writeResult(Class<?> token, Path root, String result) throws ImplerException {
        try {
            Path file = getPathToTargetFile(root, token, "java", File.separator);
            Files.createDirectories(root.resolve(token.getPackageName().replace(".", File.separator)));
            Files.createFile(file);
            try (BufferedWriter bw = Files.newBufferedWriter(file)) {
                bw.write(toUnicode(result));
            }
        } catch (IOException e) {
            throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
        }
    }

    private String toUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        for (final char c : str.toCharArray()) {
            if (c >= 128) {
                sb.append(String.format("\\u%04X", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * returns all <code>public</code> <code>protected</code> and <code>package-private</code> methods
     * of given class and its parents.
     *
     * @param token class to get methods from.
     * @return {@link Stream} of wrapper objects over methods of class represented by given type token.
     */
    private Stream<MethodWrapper> getAllMethods(Class<?> token) {
        Stream<MethodWrapper> processedMethods = Arrays.stream(token.getMethods()).map(MethodWrapper::new);
        while (token != null) {
            processedMethods = Stream.concat(processedMethods,
                    Arrays.stream(token.getDeclaredMethods())
                            .filter(m -> {
                                int modifiers = m.getModifiers();
                                return !Modifier.isPrivate(modifiers) && !Modifier.isPublic(modifiers);
                            }).map(MethodWrapper::new));
            token = token.getSuperclass();
        }
        return processedMethods.distinct().filter(m -> Modifier.isAbstract(m.method.getModifiers()));
    }

    private int removeUnAllowedModifiers(int modifiers) {
        return modifiers & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT;
    }

    /**
     * Used for creation of code of single {@link Executable}
     *
     * @param executable {@link Method} or {@link Constructor}
     * @param name       supplier of the name of {@link Executable}
     * @param format     formatting pattern
     * @param arguments  arguments of {@link Executable}
     * @param <T>        child of {@link Executable}
     * @return formatted {@link String}
     */
    private <T extends Executable> String executablesCode(T executable, String name, String format, String arguments, String modifiers) {
        return String.format(format, modifiers, name, joinParameters(executable.getParameters()),
                joinExceptions(executable.getExceptionTypes()), arguments);
    }

    /**
     * produces string from given array
     *
     * @param arr array to create stream.
     * @param fn  function to apply in {@link Stream#map(Function)}.
     * @param <T> type of the array.
     * @return {@link String} built of items of array after application of fn and {@link Object#toString()} on each and joined
     * using <code>", "</code> delimiter.
     */
    private <T> String streamMapAndJoin(T[] arr, Function<T, String> fn) {
        return Arrays.stream(arr).map(fn).collect(Collectors.joining(", "));
    }

    /**
     * Same as {@link Implementor} but accepts stream as parameter instead of array and joins items using
     * <code>""</code> delimiter.
     *
     * @param stream stream to generate string.
     * @param fn     function to apply on each item in map.
     * @param <T>    generic child class of Executable {@link Method} or {@link Constructor}
     * @return {@link String} produced by applying this operations.
     */
    private <T extends Executable> String mapAndJoin(Stream<T> stream, Function<T, String> fn) {
        return stream.map(fn).collect(Collectors.joining());
    }

    /**
     * Concatenates exceptions to string
     *
     * @param exceptions array of types of exceptions
     * @return a string of concatenated exceptions
     */
    private String joinExceptions(Class<?>[] exceptions) {
        return exceptions.length > 0 ?
                String.format("throws %s", streamMapAndJoin(exceptions, Class::getCanonicalName)) : "";
    }

    /**
     * Joins parameters into string.
     *
     * @param parameters array of parameters.
     * @return string of parameters joined in pattern type-name.
     * using {@link Implementor#streamMapAndJoin(Object[], Function)}.
     */
    private String joinParameters(Parameter[] parameters) {
        return streamMapAndJoin(parameters, i -> String.format("%s %s", i.getType().getCanonicalName(), i.getName()));
    }

    /**
     * returns default value of given type.
     *
     * @param clazz type to get default value.
     * @return default value of given type.
     */
    private String getDefaultValue(Class<?> clazz) {
        return clazz != void.class ? clazz == boolean.class ? "false" : !clazz.isPrimitive() ? "null" : "0" : "";
    }

    /**
     * Produces a code of constructors of given class.
     *
     * @param constructors class to implement constructors code.
     * @return string containing a code of implemented constructors.
     */
    private String constructorsCode(Constructor<?>[] constructors) {
        return mapAndJoin(Arrays.stream(constructors),
                m -> executablesCode(m, m.getDeclaringClass().getSimpleName(), "%s %sImpl(%s) %s {%n super(%s);%n}%n",
                        streamMapAndJoin(m.getParameters(), Parameter::getName),
                        Modifier.toString(removeUnAllowedModifiers(m.getModifiers()))));
    }

    /**
     * Similar to {@link Implementor#constructorsCode(Constructor[])} but produces code of methods.
     *
     * @param token class to implement methods code.
     * @return string of implemented methods.
     */
    private String methodsCode(Class<?> token) {
        return mapAndJoin(getAllMethods(token).map(MethodWrapper::getMethod),
                m -> executablesCode(m, m.getName(), "%s %s(%s) %s {%n return %s;%n}%n",
                        getDefaultValue(m.getReturnType()),
                        String.format("%s %s", Modifier.toString(removeUnAllowedModifiers(m.getModifiers())),
                                m.getReturnType().getCanonicalName())));
    }

    /**
     * Class wrapper for method object used for {@link Method} introduced to override
     * {@link Method#equals(Object)} and {@link Method#hashCode()}.
     *
     * @see Method
     */
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
                if (this.hashCode() != other.hashCode())
                    return false;
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