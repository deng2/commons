package com.greenbee.commons;

public class ClassLib {

    public static final String CLASSFILE_SUFFIX = ".class";

    /**
     * @param clazzFilePath - class path like com/vitria/test/TestLib.class
     * @return class name like com.vitria.test.TestLib
     */
    public static String toClassName(String clazzFilePath) {
        String clazzName = clazzFilePath.replace("/", ".").replace("\\", ".");
        if (clazzName.startsWith("."))
            clazzName = clazzName.substring(1);
        if (clazzName.endsWith(CLASSFILE_SUFFIX))
            clazzName = clazzName.substring(0, clazzFilePath.length() - CLASSFILE_SUFFIX.length());
        return clazzName;
    }

    public static String toClassFilePath(Class<?> clazz) {
        String clazzName = clazz.getName();

        return toClassFilePath(clazzName);
    }

    public static String toClassFilePath(String clazzName) {
        String classPath = clazzName.replace(".", "/");
        classPath = classPath.concat(CLASSFILE_SUFFIX);
        return classPath;
    }

    public static String toClassShortName(Class<?> clazz) {
        String clazzName = clazz.getName();
        int index = clazzName.lastIndexOf(".");
        return index < 0 ? clazzName : clazzName.substring(index + 1);
    }

    public static String toClassPackageName(Class<?> clazz) {
        return clazz.getPackage().getName();
    }

    public static void main(String[] args) {
        String classFilePath = toClassFilePath(ClassLib.class);
        System.out.println(classFilePath);
        System.out.println(toClassName(classFilePath));
        System.out.println(toClassShortName(ClassLib.class));
    }
}
