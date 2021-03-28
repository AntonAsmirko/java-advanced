package info.kgeorgiy.ja.asmirko.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

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
    private static final String SPACE = " ";

    private static final String BOOL_DEFAULT = "false";
    private static final String INT_DEFAULT = "0";
    private static final String SHORT_DEFAULT = "0";
    private static final String BYTE_DEFAULT = "0";
    private static final String LONG_DEFAULT = "0";
    private static final String OBJ_DEFAULT = "null";
    private static final String CHAR_DEFAULT = "0";
    private static final String FLOAT_DEFAULT = "0.0f";
    private static final String DOUBLE_DEFAULT = "0.0";

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        StringBuilder classCode = new StringBuilder();

        String packageName = token.getPackageName();
        classCode.append(String.format("%s %s; %s", PACKAGE_KEYWORD, packageName, System.lineSeparator()));

        int modifiers = token.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            throw new ImplerException();
        }
        if (Modifier.isPublic(modifiers)) {
            classCode.append(String.format("%s ", PUBLIC_KEYWORD));
        }
        classCode.append(String.format("%s ", CLASS_KEYWORD));

        String className = token.getSimpleName();
        String classNameCanonical = token.getCanonicalName();
        if (token.isPrimitive()) {
            throw new ImplerException();
        }
        classCode.append(
                String
                        .format(
                                "%s%s %s %s {%s%s",
                                className,
                                CLASS_SUFFIX,
                                IMPLEMENTS_KEYWORD,
                                classNameCanonical,
                                System.lineSeparator(),
                                System.lineSeparator()
                        )
        );

        Method[] methods = token.getMethods();
        for (Method m : methods) {
            int mModifiers = m.getModifiers();
            classCode.append(TAB);

            if (Modifier.isPublic(mModifiers))
                classCode.append(String.format("%s ", PUBLIC_KEYWORD));

            if (Modifier.isProtected(mModifiers))
                classCode.append(String.format("%s ", PROTECTED_KEYWORD));


            Class<?> returnType = m.getReturnType();

            classCode.append(
                    String
                            .format(
                                    "%s ", returnType.isPrimitive() ?
                                            returnType.getName() :
                                            returnType.getCanonicalName()
                            )
            );

            String mName = m.getName();
            classCode
                    .append(mName)
                    .append("(");

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
                                    INT_DEFAULT));
                } else {
                    classCode.append(String.format(" %s", OBJ_DEFAULT));
                }
            }

            classCode.append(String.format(";%s%s}%s", System.lineSeparator(), TAB, System.lineSeparator()));
        }
        classCode.append("}");
        Path path = getPath(root, token);
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {

        }
        try (BufferedWriter bw = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.WRITE
        )) {
            bw.write(classCode.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    protected Path getPath(Path path, Class<?> token) {
        return Paths
                .get(path.toString(), token.getPackageName().replaceAll("\\.", "\\" + File.separator), token.getSimpleName() + "Impl." + "java");
    }
}
