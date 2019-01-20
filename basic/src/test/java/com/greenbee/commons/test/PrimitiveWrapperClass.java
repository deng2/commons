package com.greenbee.commons.test;

import java.util.LinkedHashSet;
import java.util.Set;

public class PrimitiveWrapperClass {
    @SuppressWarnings({ "rawtypes" })
    private static Set<Class> classes_ = new LinkedHashSet<Class>();

    static {
        classes_.add(Byte.TYPE);
        classes_.add(Short.TYPE);
        classes_.add(Integer.TYPE);
        classes_.add(Long.TYPE);
        classes_.add(Float.TYPE);
        classes_.add(Double.TYPE);
    }

    @SuppressWarnings({ "rawtypes" })
    public static void main(String[] args) throws Exception {
        for (Class clazz : classes_)
            System.out.println(clazz.getName());
    }

}
