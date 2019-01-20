package com.greenbee.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes") public class JavaBeanUtil {

    public static void configureJavaBean(Object bean, Map pairs) throws Exception {
        @SuppressWarnings("unchecked") Set<Map.Entry<String, Object>> entries = pairs.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String name = entry.getKey();
            Object value = entry.getValue();
            configureJavaBeanProperty(bean, name, value);
        }

    }

    /**
     *
     * @param bean
     * @param name
     * @param value
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     *   String type JavaBean property is allowed
     */

    @SuppressWarnings("unchecked") private static void configureJavaBeanProperty(Object bean,
            String name, Object value)
            throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        String methodName = getPropertyAccessMethodName(name);
        Class clazz = bean.getClass();
        Class param = value.getClass();
        Class primitive = getPrimitiveType(param);
        Method method;
        try {
            method = clazz.getMethod(methodName, new Class[] { primitive });
        } catch (NoSuchMethodException nme) {
            method = clazz.getMethod(methodName, new Class[] { param });
        }
        method.invoke(bean, new Object[] { value });
    }

    /**
     * @param JavaBean property name
     * @return JavaBean's access method name
     */
    private static String getPropertyAccessMethodName(String name) {
        char start = name.charAt(0);
        if (!Character.isUpperCase(start)) {
            return "set" + Character.toUpperCase(start) + name.substring(1);
        } else {
            return "set" + name;
        }
    }

    protected static Class getPrimitiveType(Class clazz) {
        if (clazz == Integer.class)
            return Integer.TYPE;
        else if (clazz == Double.class)
            return Double.TYPE;
        else if (clazz == Byte.class)
            return Byte.TYPE;
        else if (clazz == Short.class)
            return Short.TYPE;
        else if (clazz == Long.class)
            return Long.TYPE;
        else if (clazz == Boolean.class)
            return Boolean.TYPE;
        else if (clazz == Float.class)
            return Float.TYPE;
        return clazz;
    }
}
