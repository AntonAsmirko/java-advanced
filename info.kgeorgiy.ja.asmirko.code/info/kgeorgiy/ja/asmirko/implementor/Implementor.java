package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;

public class Implementor implements Impler {

    private static final String PUBLIC_KEYWORD = "public";
    private static final String PROTECTED_KEYWORD = "protected";
    private static final String CLASS_KEYWORD = "class";
    private static final String IMPLEMENTS_KEYWORD = "implements";
    private static final String RETURN_KEYWORD = "return";
    private static final String PACKAGE_KEYWORD = "package";
    private static final String CLASS_SUFFIX = "Impl";
    private static final String ARG = "arg";
    private static final String TAB = "    ";
    private static final String BOOL_DEFAULT = "false";
    private static final String OTHER_PRIMITIVE_DEFAULT = "0";
    private static final String OBJ_DEFAULT = "null";

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        StringBuilder classCode = new StringBuilder();

        classCode.append(String.format("%s %s; %s", PACKAGE_KEYWORD, Optional.of(token.getPackageName()).orElse(""), System.lineSeparator()));

        int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            throw new ImplerException();
        }
        if (Modifier.isPublic(modifiers)) {
            classCode.append(String.format("%s ", PUBLIC_KEYWORD));
        }
        classCode.append(String.format("%s ", CLASS_KEYWORD));

        if (token.isPrimitive()) {
            throw new ImplerException();
        }
        classCode.append(
                String
                        .format(
                                "%s%s %s %s {%s%s",
                                token.getSimpleName(),
                                CLASS_SUFFIX,
                                IMPLEMENTS_KEYWORD,
                                token.getCanonicalName(),
                                System.lineSeparator(),
                                System.lineSeparator()
                        )
        );

        Arrays.stream(token.getMethods()).forEach(m -> {
            int mModifiers = m.getModifiers();
            classCode.append(TAB);

            if (Modifier.isPublic(mModifiers))
                classCode.append(String.format("%s ", PUBLIC_KEYWORD));

            if (Modifier.isProtected(mModifiers))
                classCode.append(String.format("%s ", PROTECTED_KEYWORD));


            Class<?> returnType = m.getReturnType();
            classCode.append(String.format("%s ", returnType.isPrimitive() ?
                    returnType.getName() :
                    returnType.getCanonicalName())
            );
            classCode.append(String.format("%s(", m.getName()));
            Class<?>[] mParameterTypes = m.getParameterTypes();
            for (int i = 0; i < mParameterTypes.length; i++) {
                classCode.append(String.format("%s %s%d", mParameterTypes[i].getCanonicalName(), ARG, i + 1));

                if (i != mParameterTypes.length - 1)
                    classCode.append(", ");
            }
            classCode.append(String.format("){%s%s%s%s", System.lineSeparator(), TAB, TAB, RETURN_KEYWORD));
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
            classCode.append(String.format(";%s%s}%s", System.lineSeparator(), TAB, System.lineSeparator()));
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
            Files.createFile(path);
        } catch (
                IOException e) {
            throw new IllegalStateException(String.format("Internal error, can't create file to write class, internal exception message: %s", e.getMessage()));
        }
        try (BufferedWriter bw = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.WRITE
        )) {
            bw.write(classCode.toString());
        } catch (
                IOException e) {
            throw new IllegalStateException(String.format("Internal error, unable to create StringBuilder or perform writing operation, internal exception message: %s", e.getMessage()));
        }
    }
}
