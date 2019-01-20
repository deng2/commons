// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLib {
    public static String getParentPath(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        return parent.getPath();
    }

    public static void mkParent(String filePath) throws IOException {
        mkDir(getParentPath(filePath));

    }

    public static void mkDir(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() && !file.mkdirs())
            throw new IOException("Fail to create parent folder " + file.getPath());
    }

    public static void rmDir(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists())
            return;
        if (dir.isFile())
            throw new IOException("Directory " + dir.getPath() + " is a file");
        File[] files = dir.listFiles();
        for (File file : files) {
            String path = file.getPath();
            if (file.isDirectory())
                rmDir(path);
            else
                rm(path);
        }
        boolean success = dir.delete();
        if (!success)
            throw new IOException("Fail to delete directory " + dir.getPath());
    }

    public static void rm(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists())
            return;
        if (file.isDirectory())
            throw new IOException("File " + file.getPath() + " is a directory");
        boolean success = file.delete();
        if (!success)
            throw new IOException("Fail to delete file " + file.getPath());
    }

    public static List<String> listClassNames(String dirPath) {
        String dir = new File(dirPath).getPath();
        List<String> files = listClassFiles(dirPath, true);

        List<String> clazzes = new ArrayList<>();
        for (String file : files) {
            String classPath = file.substring(dir.length() + 1);
            String clazzName = ClassLib.toClassName(classPath);
            clazzes.add(clazzName);
        }
        return clazzes;
    }

    public static List<String> listClassFiles(String dirPath, boolean recursive) {
        return listFiles(dirPath, recursive, new ClassFileFilter());
    }

    public static List<String> listJarFiles(String dirPath, boolean recursive) {
        return listFiles(dirPath, recursive, new JarFileFilter());
    }

    private static List<String> listFiles(String dirPath, boolean recursive, FileFilter filter) {
        List<String> paths = new ArrayList<String>();
        File dir = new File(dirPath);
        if (!dir.isDirectory())//to exclude none existing path
            return paths;
        File[] files = dir.listFiles(filter);
        for (File file : files) {
            String path = file.getPath();
            if (file.isFile())
                paths.add(path);
            else if (recursive) {
                List<String> subPaths = listFiles(path, recursive, filter);
                paths.addAll(subPaths);
            }
        }
        return paths;
    }

    public static String join(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            if (i != 0)
                sb.append("/");
            sb.append(paths[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(listJarFiles("D:\\yoda\\export\\home\\jboss", true));
        System.out.println(listClassFiles("D:\\yoda\\export\\test\\classes", true));
        System.out.println(listClassNames("D:/yoda\\export\\test\\classes"));
        System.out.println(getParentPath("D:/yoda\\export\\test\\classes"));
    }

    protected static final class JarFileFilter implements FileFilter {
        @Override public boolean accept(File file) {
            return file.isDirectory() || (file.isFile() && file.getName().endsWith(".jar"));
        }
    }

    protected static final class ClassFileFilter implements FileFilter {
        @Override public boolean accept(File file) {
            return file.isDirectory() || (file.isFile() && file.getName().endsWith(".class"));
        }
    }

}
