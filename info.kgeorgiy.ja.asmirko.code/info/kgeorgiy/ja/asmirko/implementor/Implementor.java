package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import javax.tools.*;

public class Implementor implements JarImpler {

    private static final String PUBLIC_KEYWORD = "public";
    private static final String BOOL_DEFAULT = "false";
    private static final String OTHER_PRIMITIVE_DEFAULT = "0";
    private static final String OBJ_DEFAULT = "null";

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        StringBuilder classCode = new StringBuilder();

        classCode.append(String.format("%s %s; %s", "package", Optional.of(token.getPackageName()).orElse(""), System.lineSeparator()));

        int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            throw new ImplerException();
        }
        if (Modifier.isPublic(modifiers)) {
            classCode.append(String.format("%s ", PUBLIC_KEYWORD));
        }
        classCode.append(String.format("%s ", "class"));

        if (token.isPrimitive()) {
            throw new ImplerException();
        }
        classCode.append(
                String.format(
                        "%s%s %s %s {%s%s",
                        token.getSimpleName(),
                        "Impl",
                        "implements",
                        token.getCanonicalName(),
                        System.lineSeparator(),
                        System.lineSeparator()
                )
        );

        Arrays.stream(token.getMethods()).forEach(m -> {
            int mModifiers = m.getModifiers();
            classCode.append("    ");

            if (Modifier.isPublic(mModifiers))
                classCode.append(String.format("%s ", PUBLIC_KEYWORD));

            if (Modifier.isProtected(mModifiers))
                classCode.append(String.format("%s ", "protected"));


            Class<?> returnType = m.getReturnType();
            classCode.append(String.format("%s ", returnType.isPrimitive() ?
                    returnType.getName() :
                    returnType.getCanonicalName())
            );
            classCode.append(String.format("%s(", m.getName()));
            Class<?>[] mParameterTypes = m.getParameterTypes();
            for (int i = 0; i < mParameterTypes.length; i++) {
                classCode.append(String.format("%s %s%d", mParameterTypes[i].getCanonicalName(), "arg", i + 1));

                if (i != mParameterTypes.length - 1)
                    classCode.append(", ");
            }
            classCode.append(String.format("){%s        %s", System.lineSeparator(), "return"));
            if (!returnType.getName().equals(void.class.getName())) {

                if (returnType.isPrimitive()) {
                    classCode.append(String.format(" %s",
                            returnType.getName().equals(boolean.class.getName()) ?
                                    BOOL_DEFAULT :
                                    OTHER_PRIMITIVE_DEFAULT));
                } else {
                    classCode.append(String.format(" %s", OBJ_DEFAULT));
                }
            }
            classCode.append(String.format(";%s    }%s", System.lineSeparator(), System.lineSeparator()));
        });

        classCode.append("}");
        Path path = Paths
                .get(root.toString(),
                        token
                                .getPackageName()
                                .replaceAll("\\.", "\\" + File.separator),
                        token.getSimpleName() + "Impl." + "java");
        try {
            Files.createDirectories(path.getParent());
            //Files.createFile(path);
        } catch (
                IOException e) {
            e.printStackTrace();
            throw new ImplerException(String.format("Internal error, can't create file to write class, internal exception message: %s", e.getMessage()));
        }
        try (BufferedWriter bw = Files.newBufferedWriter(
                path
        )) {
            bw.write(classCode.toString());
        } catch (
                IOException e) {
            e.printStackTrace();
            throw new ImplerException(String.format("Internal error, unable to create StringBuilder or perform writing operation, internal exception message: %s", e.getMessage()));
        }
    }

    private Path getPathToSomeShit(Path jarFile, Class<?> token, String fileExtension) {
        return Paths
                .get(jarFile.toString(),
                        token
                                .getPackageName()
                                .replaceAll("\\.", "\\" + File.separator),
                        token.getSimpleName() + "Impl." + fileExtension);
    }

    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        Path dir = jarFile.getParent();
        implement(token, dir);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try {
            if (compiler.run(null, null, null, "-cp", Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString(),
                    getPathToSomeShit(jarFile, token, "java").toString()) == 0) {
                try (
                        var jOS = new JarOutputStream(
                                Files.newOutputStream(
                                        jarFile,
                                        StandardOpenOption.WRITE
                                )
                        )
                ) {
                    jOS.putNextEntry(new ZipEntry(getPathToSomeShit(Path.of(""), token, "class").toString()));
                    Files.copy(getPathToSomeShit(jarFile, token, "class"), jOS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ImplerException();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
