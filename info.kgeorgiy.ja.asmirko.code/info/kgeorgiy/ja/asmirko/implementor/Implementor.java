package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Implementor implements Impler {

    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        if (!token.isInterface()) {
            throw new ImplerException("token should be interface");
        }
        final int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            throw new ImplerException("Can't implement private interface");
        }
        final StringBuilder classCode = new StringBuilder();
        classCode.append(String.format("package %s; %n", token.getPackageName()));

        if (Modifier.isPublic(modifiers)) {
            classCode.append("public ");
        }

        classCode.append(String.format("class %sImpl implements %s {%n%n", token.getSimpleName(), token.getCanonicalName()));

        Arrays.stream(token.getMethods()).forEach(m -> {
            Arrays.stream(m.getAnnotations())
                    .forEach(annotation
                            -> classCode.append(String.format("@%s%n", annotation.annotationType().getName())));
            final int mModifiers = m.getModifiers();
            classCode.append("    ");

            if (Modifier.isPublic(mModifiers))
                classCode.append("public ");

            final Class<?> returnType = m.getReturnType();
            final Class<?>[] mParameterTypes = m.getParameterTypes();
            classCode.append(String.format("%s %s(%s){%n    return", returnType.getCanonicalName(), m.getName(),
                    IntStream.range(0, mParameterTypes.length)
                            .mapToObj(i -> String.format("%s arg%d", mParameterTypes[i].getCanonicalName(), i + 1))
                            .collect(Collectors.joining(", "))));
            if (!returnType.getName().equals(void.class.getName())) {
                if (returnType.isPrimitive()) {
                    classCode.append(String.format(" %s", returnType.isAssignableFrom(boolean.class) ? "false" : "0"));
                } else {
                    classCode.append(" null");
                }
            }
            classCode.append(String.format(";%n    }%n"));
        });

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
}