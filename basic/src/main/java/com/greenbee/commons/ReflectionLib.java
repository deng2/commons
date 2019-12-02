package com.greenbee.commons;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionLib {

    public static Class<?> loadClass(ClassLoader cl, String name) throws ClassNotFoundException {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        if (ccl == cl)
            cl = null;//avoid unnecessary duplicate load
        try {
            return Class.forName(name, true, ccl);
        } catch (ClassNotFoundException cne) {
            if (cl != null)
                return Class.forName(name, true, cl);
            throw cne;
        }
    }

    public static Object createInstance(String clazzName) throws Exception {
        return createInstance(null, clazzName, new Class<?>[0], new Object[0]);
    }

    public static Object createInstance(ClassLoader cl, String clazzName) throws Exception {
        return createInstance(cl, clazzName, new Class<?>[0], new Object[0]);
    }

    public static Object createInstance(ClassLoader cl, String clazzName, Class<?>[] types,
            Object[] params) throws Exception {
        Class<?> clazz = loadClass(cl, clazzName);
        return createInstance(clazz, types, params);
    }

    public static Object createInstance(Class<?> clazz) throws Exception {
        return createInstance(clazz, new Class<?>[0], new Object[0]);
    }

    public static Object createInstance(Class<?> clazz, Class<?>[] types, Object[] params)
            throws Exception {
        Constructor<?> cst = clazz.getConstructor(types);
        try {
            return cst.newInstance(params);
        } catch (Exception e) {
            throw wrapAndThrow(e, cst.getExceptionTypes());
        }
    }

    public static Object invoke(Object instance, String methodName) throws Exception {
        return invoke(instance, methodName, new Class<?>[0], new Object[0]);
    }

    public static Object invoke(Object instance, String methodName, Class<?>[] types,
            Object[] params) throws Exception {
        Class<?> clazz = instance.getClass();
        Method method = clazz.getMethod(methodName, types);
        return invoke(instance, method, params);
    }

    public static Object invoke(Object instance, Method method, Object... params) throws Exception {
        try {
            if (!verifyModifier(method.getModifiers(), ~Modifier.STATIC))
                throw new Exception("Static method");
            return method.invoke(instance, params);
        } catch (Exception e) {
            throw wrapAndThrow(e, method.getExceptionTypes());
        }
    }

    public static Object invokeStatic(ClassLoader cl, String className, String methodName)
            throws Exception {
        return invokeStatic(cl, className, methodName, new Class<?>[0], new Object[0]);
    }

    public static Object invokeStatic(ClassLoader cl, String className, String methodName,
            Class<?>[] types, Object[] params) throws Exception {
        Class<?> clazz = loadClass(cl, className);
        Method method = clazz.getMethod(methodName, types);
        return invokeStatic(method, params);
    }

    public static Object invokeStatic(Method method, Object... params) throws Exception {
        try {
            if (!verifyModifier(method.getModifiers(), Modifier.STATIC))
                throw new Exception("Not static method");
            return method.invoke(null, params);
        } catch (Exception e) {
            throw wrapAndThrow(e, method.getExceptionTypes());
        }
    }

    public static Object get(Object instance, String fieldName) throws Exception {
        Class<?> clazz = instance.getClass();
        Field field = clazz.getField(fieldName);
        if (!verifyModifier(field.getModifiers(), ~Modifier.STATIC))
            throw new Exception("Static method");
        return field.get(instance);
    }

    public static Object getDeclared(Object instance, String fieldName) throws Exception {
        Class<?> clazz = instance.getClass();
        Field field = clazz.getDeclaredField(fieldName);
        if (!verifyModifier(field.getModifiers(), ~Modifier.STATIC))
            throw new Exception("Static method");
        field.setAccessible(true);
        return field.get(instance);
    }

    public static Object getStatic(ClassLoader cl, String className, String fieldName)
            throws Exception {
        Class<?> clazz = loadClass(cl, className);
        return getStatic(clazz, fieldName);
    }

    public static Object getStatic(Class<?> clazz, String fieldName)
            throws NoSuchFieldException, Exception, IllegalAccessException {
        Field field = clazz.getField(fieldName);
        if (!verifyModifier(field.getModifiers(), Modifier.STATIC))
            throw new Exception("Not static method");
        return field.get(null);
    }

    public static boolean verifyModifier(int input, int... conditions) {
        for (int condition : conditions) {
            if ((input & condition) == 0)
                return false;
        }
        return true;
    }

    public static List<Method> getAnnotatedMethod(Class<?> clazz,
            Class<? extends Annotation> annoClazz) {
        return getAnnotatedMethod(clazz, annoClazz, ~Modifier.STATIC, Modifier.PUBLIC);
    }

    /**
     * two user case:
     * 1. annotation defined on the method
     * 2. annotation defined on the override method of parent class
     */
    public static List<Method> getAnnotatedMethod(Class<?> clazz,
            Class<? extends Annotation> annoClazz, int... conditions) {
        List<Method> rtn = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!verifyModifier(method.getModifiers(), conditions))
                continue;
            if (getAnnotation(method, annoClazz) == null)
                continue;
            rtn.add(method);
        }
        return rtn;
    }

    public static Annotation getAnnotation(Method method, Class<? extends Annotation> annoClazz) {
        while (true) {
            if (method.isAnnotationPresent(annoClazz))//found
                break;
            Class<?> superClazz = method.getDeclaringClass().getSuperclass();
            if (superClazz == null)//not found
                break;
            try {
                method = superClazz.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException nme) {
                break;//not found
            }
        }
        return method.getAnnotation(annoClazz);
    }

    public static void setBeanProperty(Object bean, String name, Object value) throws Exception {
        Class<?> beanClazz = bean.getClass();
        String methodName = "set" + name;
        Method method;
        Class<? extends Object> argClazz = value.getClass();
        try {
            Class<?>[] types = new Class<?>[] { argClazz };
            method = beanClazz.getMethod(methodName, types);
        } catch (NoSuchMethodException nme) {
            Class<?> pmvClazz = getPrimitiveClass(argClazz);
            if (pmvClazz != null) {
                method = beanClazz.getMethod(methodName, pmvClazz);
            } else {
                throw nme;
            }
        }
        try {
            method.invoke(bean, value);
        } catch (Exception e) {
            throw wrapAndThrow(e, method.getExceptionTypes());
        }
    }

    protected static Class<?> getPrimitiveClass(Class<?> argClazz) {
        Class<?> pmvClazz;
        if (Boolean.class == argClazz) {
            pmvClazz = Boolean.TYPE;
        } else if (Character.class == argClazz) {
            pmvClazz = Character.TYPE;
        } else if (Byte.class == argClazz) {
            pmvClazz = Byte.TYPE;
        } else if (Short.class == argClazz) {
            pmvClazz = Short.TYPE;
        } else if (Integer.class == argClazz) {
            pmvClazz = Integer.TYPE;
        } else if (Long.class == argClazz) {
            pmvClazz = Long.TYPE;
        } else if (Float.class == argClazz) {
            pmvClazz = Float.TYPE;
        } else if (Double.class == argClazz) {
            pmvClazz = Double.TYPE;
        } else {
            pmvClazz = null;
        }
        return pmvClazz;
    }

    //get real exception and wrapAndThrow exception into runtime exception if it is not declared
    protected static Exception wrapAndThrow(Exception ex, Class<?>[] exceptions) throws Exception {
        Throwable cause = ex;
        if (ex instanceof InvocationTargetException) {//get target exception
            cause = ((InvocationTargetException) ex).getTargetException();
        }
        //throw unchecked exception
        if (cause instanceof Error)
            throw (Error) cause;
        if (cause instanceof RuntimeException)
            throw (RuntimeException) cause;
        //throw declared exception
        if (cause instanceof Exception) {
            for (Class<?> exception : exceptions) {
                if (exception.isAssignableFrom(cause.getClass()))
                    throw (Exception) cause;
            }
        }
        throw new RuntimeException(cause);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(get(new Test(), "b"));
        System.out.println(getDeclared(new Test(), "c"));
        System.out.println(getStatic(Test.class, "a"));
        ReflectionLib.invokeStatic(ReflectionLib.class.getClassLoader(), "com.greenbee.commons.ReflectionLib$Test", "testException");
    }

    @SuppressWarnings("FieldCanBeLocal")
    public static class Test {
        public static String a = "a";
        public String b = "b";
        private String c = "c";

        public String getC() {
            return c;
        }

        public static void testException() throws Exception {
            throw new Exception("testException");
        }
    }
}
