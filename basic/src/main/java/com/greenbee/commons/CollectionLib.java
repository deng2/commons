package com.greenbee.commons;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class CollectionLib {

    public static <K, V> Properties toProperties(Map<K, V> map) {
        Properties convert = new Properties();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            convert.setProperty(key, value);
        }
        return convert;
    }

    public static <K, V> V getValue(Map<K, V> map, K key, V defaultValue) {
        V value = map.get(key);
        return value == null ? defaultValue : value;
    }

    public static <V> Map<String, V> getSubMap(Map<String, V> map, String prefix) {
        //preserve order
        LinkedHashMap<String, V> rtn = new LinkedHashMap<>();
        for (Map.Entry<String, V> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(prefix))
                rtn.put(key, entry.getValue());
        }
        return rtn;
    }

    public static Properties getProperties(Properties whole, String prefix) {
        Properties sorts = createSortedProperties();//pay attention the order
        Set<String> names = whole.stringPropertyNames();
        for (String name : names) {
            if (!name.startsWith(prefix))
                continue;
            sorts.setProperty(name.substring(prefix.length()), whole.getProperty(name));
        }
        return sorts;
    }

    public static URL[] toUrls(List<String> paths) throws MalformedURLException {
        List<File> jars = new ArrayList<File>();
        for (String path : paths) {
            File file = new File(path);
            jars.add(file);
        }
        ArrayList<URL> urls = new ArrayList<URL>();
        for (File file : jars) {
            urls.add(file.toURI().toURL());
        }
        return urls.toArray(new URL[0]);
    }

    public static <K> List<K> unique(List<K> input) {
        LinkedHashSet<K> set = new LinkedHashSet<K>();
        for (K mem : input) {
            set.add(mem);
        }
        return new ArrayList<K>(set);
    }

    public static Properties createSortedProperties() {
        return new SortedProperties();
    }

    public static Map<String, String> toMap(Properties properties) {
        Map<String, String> map = new HashMap<String, String>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            map.put(key, value);
        }
        return map;
    }

    public static class SortedProperties extends Properties {
        private static final long serialVersionUID = 1L;
        private LinkedHashMap<Object, Object> delegate_ = new LinkedHashMap<Object, Object>();

        private SortedProperties() {
        }

        @Override public Enumeration<?> propertyNames() {
            return Collections.enumeration(delegate_.keySet());
        }

        @Override public Set<String> stringPropertyNames() {
            LinkedHashSet<String> keys = new LinkedHashSet<String>();
            for (Map.Entry<Object, Object> entry : entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String && value instanceof String)
                    keys.add((String) key);
            }
            return keys;
        }

        public Enumeration<Object> keys() {
            return Collections.enumeration(delegate_.keySet());
        }

        @Override public Set<Object> keySet() {
            return delegate_.keySet();
        }

        @Override public Set<Entry<Object, Object>> entrySet() {
            return delegate_.entrySet();
        }

        public Object put(Object key, Object value) {
            delegate_.put(key, value);
            return super.put(key, value);
        }

        @Override public synchronized Object remove(Object key) {
            delegate_.remove(key);
            return super.remove(key);
        }

        @Override public synchronized void clear() {
            delegate_.clear();
            super.clear();
        }

    }
}
