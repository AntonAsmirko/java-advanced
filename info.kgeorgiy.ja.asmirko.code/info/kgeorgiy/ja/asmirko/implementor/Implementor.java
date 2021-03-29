package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

public class Implementor implements Impler {

    private static final String PUBLIC_KEYWORD = "public";
    private static final String BOOL_DEFAULT = "false";
    private static final String OTHER_PRIMITIVE_DEFAULT = "0";
    private static final String OBJ_DEFAULT = "null";

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        StringBuilder classCode = new StringBuilder();
        Optional.of(token.getPackageName()).ifPresent(s -> {
            classCode.append(String.format("package %s; %s", Optional.of(token.getPackageName()).orElse(""), System.lineSeparator()));
        });
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
//            Arrays.stream(m.getAnnotations())
//                    .forEach(annotation -> {});
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
        try (BufferedWriter bw = Files.newBufferedWriter(path)){
            Files.createDirectories(path.getParent());
            bw.write(classCode.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
        }
    }
}
