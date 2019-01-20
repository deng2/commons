package com.greenbee.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoaderUtil {

    /**
     *
     * @param cl - the class loader
     * @param path - used to identify path of the resource.
     *          E.g.: com/vitria/util/bpmn.properties
     * @return InputStream of the resource, null if no resource found.
     * <pre>
     *  use current thread's context ClassLoader as resource finding ClassLoader. 
     *  basically, ClassLoader's hierarchy is: 
     *      system class loader
     *          <-  ..
     *          <-  currentThread context ClassLoader
     *  resource finding order is: 
     *      1. boot strap class loader - VM built-in class loader 
     *      2. system class loader 
     *      3. .. 
     *      4. current thread context ClassLoader
     *  first suitable resource will serve request         
     *</pre>
     */
    protected static InputStream loadStream(ClassLoader cl, String path) throws IOException {
        if (cl == null)
            cl = Thread.currentThread().getContextClassLoader();//use context class loader
        InputStream is = cl.getResourceAsStream(path);
        if (is == null) {
            throw new IOException("resource with path '" + path + "' can't located");
        }
        return is;
    }

    protected static byte[] loadBytes(ClassLoader cl, String path) throws IOException {
        InputStream is = null;
        try {
            is = loadStream(cl, path);
            return IOLib.readBytes(is);
        } finally {
            IOLib.close(is);
        }
    }

    public static InputStream loadStream(String path) throws IOException {
        return loadStream(null, path);
    }

    public static String loadString(String path) throws IOException {
        return loadString(null, path);
    }

    public static String loadString(ClassLoader cl, String path) throws IOException {
        String res = new String(loadBytes(cl, path), "UTF-8");
        return res;
    }

    public static Class<?> loadClass(String clazzName) throws ClassNotFoundException {
        return loadClass(null, clazzName);
    }

    public static Class<?> loadClass(ClassLoader cl, String clazzName)
            throws ClassNotFoundException {
        return ReflectionLib.loadClass(cl, clazzName);
    }

    public static Properties loadProperties(String path) throws IOException {
        return loadProperties(null, path);
    }

    public static Properties loadProperties(ClassLoader cl, String path) throws IOException {
        Properties env = CollectionLib.createSortedProperties();
        env.load(loadStream(cl, path));
        return env;
    }
}
