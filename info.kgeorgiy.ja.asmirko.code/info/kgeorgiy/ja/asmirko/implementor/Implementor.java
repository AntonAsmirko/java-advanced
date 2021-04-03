package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

public class Implementor implements Impler {

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        StringBuilder classCode = new StringBuilder();
        classCode.append(String.format("package %s; %n", token.getPackageName()));
        int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            throw new ImplerException();
        }
        if (Modifier.isPublic(modifiers)) {
            classCode.append("public ");
        }
        classCode.append("class ");

        if (token.isPrimitive()) {
            throw new ImplerException();
        }
        classCode.append(String.format("%sImpl implements %s {%n%n", token.getSimpleName(), token.getCanonicalName()));

        Arrays.stream(token.getMethods()).forEach(m -> {
            Arrays.stream(m.getAnnotations())
                    .forEach(annotation -> classCode.append(String.format("@%s%n", annotation.annotationType().getName())));
            int mModifiers = m.getModifiers();
            classCode.append("    ");

            if (Modifier.isPublic(mModifiers))
                classCode.append("public ");

            Class<?> returnType = m.getReturnType();
            classCode.append(String.format("%s ", returnType.isPrimitive() ?
                    returnType.getName() :
                    returnType.getCanonicalName())
            );
            classCode.append(String.format("%s(", m.getName()));
            Class<?>[] mParameterTypes = m.getParameterTypes();
            for (int i = 0; i < mParameterTypes.length; i++) {
                classCode.append(String.format("%s arg%d", mParameterTypes[i].getCanonicalName(), i + 1));

                if (i != mParameterTypes.length - 1)
                    classCode.append(", ");
            }
            classCode.append(String.format("){%n        return"));
            if (!returnType.getName().equals(void.class.getName())) {
                if (returnType.isPrimitive()) {
                    classCode.append(String.format(" %s",
                            returnType.getName().equals(boolean.class.getName()) ?
                                    "false" :
                                    "0"));
                } else {
                    classCode.append(" null");
                }
            }
            classCode.append(String.format(";%n    }%n"));
        });

        classCode.append("}");
        Path pathToFile = root.resolve(token.getPackageName().replace(".", "/"));
        Path file = pathToFile.resolve(String.format("%sImpl.java", token.getSimpleName()));
        BufferedWriter bw = null;
        try {
            Files.createDirectories(pathToFile);
            Files.createFile(file);
            bw = Files.newBufferedWriter(file);
            bw.write(classCode.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                throw new ImplerException(String.format("Internal error: %s", e.getMessage()));
            }
        }
    }
}