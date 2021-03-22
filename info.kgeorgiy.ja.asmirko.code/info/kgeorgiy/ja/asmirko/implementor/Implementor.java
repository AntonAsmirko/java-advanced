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
    /**
     * Produces code implementing class or interface specified by provided <var>token</var>.
     * <p>
     * Generated class classes name should be same as classes name of the type token with <var>Impl</var> suffix
     * added. Generated source code should be placed in the correct subdirectory of the specified
     * <var>root</var> directory and have correct file name. For example, the implementation of the
     * interface {@link List} should go to <var>$root/java/util/ListImpl.java</var>
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException when implementation cannot be
     * generated.
     */

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
    private static final String FLOAT_DEFAULT = "0.0";
    private static final String DOUBLE_DEFAULT = "0.0";

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        StringBuilder classCode = new StringBuilder();

        String packageName = token.getPackageName();
        classCode
                .append(PACKAGE_KEYWORD)
                .append(SPACE)
                .append(packageName)
                .append(";")
                .append(System.lineSeparator());

        int modifiers = token.getModifiers();
        if (Modifier.isPublic(modifiers)) {
            classCode
                    .append(PUBLIC_KEYWORD)
                    .append(SPACE);
        }
        classCode
                .append(CLASS_KEYWORD)
                .append(SPACE);

        String className = token.getSimpleName();
        String classNameCanonical = token.getCanonicalName();
        if(token.isPrimitive()){
            throw new ImplerException();
        }
        classCode
                .append(className)
                .append(CLASS_SUFFIX)
                .append(SPACE);

        classCode
                .append(IMPLEMENTS_KEYWORD)
                .append(SPACE)
                .append(classNameCanonical)
                .append(SPACE)
                .append("{")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        Method[] methods = token.getMethods();
        for (Method m : methods) {
            int mModifiers = m.getModifiers();
            classCode.append(TAB);
            if (Modifier.isPublic(mModifiers)) {
                classCode
                        .append(PUBLIC_KEYWORD)
                        .append(SPACE);
            }
            if (Modifier.isProtected(mModifiers)) {
                classCode
                        .append(PROTECTED_KEYWORD)
                        .append(SPACE);
            }

            Class<?> returnType = m.getReturnType();

            classCode.append(
                    returnType.isPrimitive() ? returnType.getName() : returnType.getCanonicalName()
            )
                    .append(SPACE);

            String mName = m.getName();
            classCode
                    .append(mName)
                    .append("(");

            Class<?>[] mParameterTypes = m.getParameterTypes();
            for (int i = 0; i < mParameterTypes.length; i++) {
                classCode
                        .append(mParameterTypes[i].getCanonicalName())
                        .append(SPACE)
                        .append(ARG)
                        .append(i + 1);
                if (i != mParameterTypes.length - 1) {
                    classCode
                            .append(",")
                            .append(SPACE);
                }
            }

            classCode
                    .append(")")
                    .append("{")
                    .append(System.lineSeparator());

            classCode
                    .append(TAB)
                    .append(TAB)
                    .append(RETURN_KEYWORD);

            if (!returnType.getName().equals(void.class.getName())) {

                if (returnType.getName().equals(int.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(INT_DEFAULT);
                } else if (returnType.getName().equals(long.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(LONG_DEFAULT);
                } else if (returnType.getName().equals(short.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(SHORT_DEFAULT);
                } else if (returnType.getName().equals(byte.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(BYTE_DEFAULT);
                } else if (returnType.getName().equals(boolean.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(BOOL_DEFAULT);
                } else if (returnType.getName().equals(double.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(DOUBLE_DEFAULT);
                } else if (returnType.getName().equals(float.class.getName())) {
                    classCode
                            .append(SPACE)
                            .append(FLOAT_DEFAULT);
                } else if (returnType.getName().equals(char.class.getName())) {
                    classCode.append(CHAR_DEFAULT);
                } else {
                    classCode
                            .append(SPACE)
                            .append(OBJ_DEFAULT);
                }
            }

            classCode.append(";");

            classCode
                    .append(System.lineSeparator())
                    .append(TAB)
                    .append("}")
                    .append(System.lineSeparator());
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
