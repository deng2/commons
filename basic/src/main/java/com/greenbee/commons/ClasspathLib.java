// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClasspathLib {
    public static final String DELIM_COLON = ":";
    public static final String DELIM_SEMICOLON = ";";
    public static final String DELIM_COMMA = ",";
    public static final String PATTERN_PATH_REF = "ref:([.,\\w\\-]*)";
    public static final String PATTERN_PATH_COLON = "(.*):(.*)";
    public static final String PATH_FOLDER_JARS = "/*";
    public static final String PATH_FOLDER_RECURSIVEJARS = "/**";
    private static final Log log_ = LogFactory.getLog(ClasspathLib.class);

    public static List<String> split(String classpaths) {
        return split(classpaths, DELIM_COMMA);
    }

    public static String join(List<String> paths) {
        return join(paths, DELIM_COMMA);
    }

    protected static List<String> split(String classpaths, String delim) {
        List<String> paths = new ArrayList<String>();
        for (String path : classpaths.split(delim)) {
            if (StringUtils.isEmpty(path))
                continue;
            paths.add(path.trim());
        }
        return paths;
    }

    protected static String join(List<String> paths, String delim) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(paths.get(i));
        }
        return sb.toString();
    }

    public static List<String> getClasspath(Map<String, List<String>> refs, String refids) {
        List<String> pathList = processRefPath(refids, refs);
        pathList = expandVar(pathList);
        pathList = processWildcardPath(pathList);
        pathList = CollectionLib.unique(pathList);
        return pathList;
    }

    /**
     * Sample: $JBOSS_HOME_LIB:common-loggs.jar,log4j.jar;$VBTA_HOME_LIB:a.jar,b.jar;refid=jboss.jndi
     *
     * ABNF: 
     * ExtPaths = Paths *[";" Paths]
     * Paths = PathRegular / PathRef
     * PathRef = "ref:" RefIds
     * RefIds = RefId *["," RefId]
     * PathsRegular = [Path ":"] PathSegment *["," PathSegment]
     * PathSegment = RegularPathSegment/FoldPathSegment/RecursiveFoldPathSegment
     * RegularPathSegment = Path
     * FoldPathSegment = Path "/*"
     * RecursiveFoldPathSegment = Path "/**"
     */

    public static Map<String, List<String>> getClassRefs(Properties def) {
        Map<String, List<String>> refs = new HashMap<String, List<String>>();
        Pattern pattern = Pattern.compile(PATTERN_PATH_REF);
        for (String key : def.stringPropertyNames()) {
            List<String> wholeList = refs.get(key);
            if (wholeList == null) {
                wholeList = new ArrayList<>();
                refs.put(key, wholeList);
            }
            for (String paths : split(def.getProperty(key), DELIM_SEMICOLON)) {
                Matcher matcher = pattern.matcher(paths);
                if (matcher.matches()) {
                    String refids = matcher.group(1);
                    wholeList.addAll(processRefPath(refids, refs));
                } else {
                    wholeList.addAll(processColonPath(paths));
                }
            }
        }
        return refs;
    }

    /**
     * refids can be comma separated list
     */
    protected static List<String> processRefPath(String refids, Map<String, List<String>> refs) {
        List<String> pathsList = new ArrayList<String>();
        for (String refid : split(refids, DELIM_COMMA)) {
            if (StringUtils.isEmpty(refid))
                continue;
            List<String> list = refs.get(refid);
            if (list != null)
                pathsList.addAll(list);
            else {
                if (log_.isWarnEnabled()) {
                    log_.warn(String.format("Classpath refid %s does not exist", refid));
                }
            }
        }
        return pathsList;
    }

    /**
     * the path is composed of path segment separated by comma prefixed with parent path 
     */
    protected static List<String> processColonPath(String paths) {
        Pattern pattern = Pattern.compile(PATTERN_PATH_COLON);
        Matcher match = pattern.matcher(paths);
        boolean hasParent = match.matches();
        String home = hasParent ? match.group(1) : null;
        String subPaths = hasParent ? match.group(2) : paths;
        List<String> classpaths = new ArrayList<String>();
        for (String pathSeg : split(subPaths, ClasspathLib.DELIM_COMMA)) {
            if (!StringUtils.isEmpty(home))
                pathSeg = FileLib.join(home, pathSeg);
            classpaths.add(pathSeg);
        }
        return classpaths;
    }

    protected static List<String> expandVar(
            List<String> classpaths) {//only get from environment variable
        String paths = ClasspathLib.join(classpaths);
        paths = EnvLib.expandVars(paths);
        List<String> pathList = ClasspathLib.split(paths);
        return pathList;

    }

    protected static List<String> processWildcardPath(List<String> pathList) {
        List<String> expandList = new ArrayList<>();
        for (String path : pathList) {
            expandList.addAll(processWildcardPath(path));
        }
        return expandList;
    }

    /**
     * support three types of class path segment
     * 1. folder like "etc"
     * 2. jars under the folder like "libs/*"
     * 3. jars under the folder recursively like "app/foudation/**"
     */
    protected static List<String> processWildcardPath(String pathSeg) {
        List<String> classpaths = new ArrayList<>();
        String path = getFolderPath(pathSeg);
        if (isFolderJarsPath(pathSeg)) {
            List<String> jars = FileLib.listJarFiles(path, false);
            classpaths.addAll(jars);
        } else if (isFolderRecursiveJarsPath(pathSeg)) {
            List<String> jars = FileLib.listJarFiles(path, true);
            classpaths.addAll(jars);
        } else {
            classpaths.add(path);
        }
        return classpaths;
    }

    protected static boolean isFolderJarsPath(String path) {
        return path.endsWith(PATH_FOLDER_JARS);
    }

    protected static boolean isFolderRecursiveJarsPath(String path) {
        return path.endsWith(PATH_FOLDER_RECURSIVEJARS);
    }

    protected static String getFolderPath(String path) {
        int len;
        if (isFolderJarsPath(path)) {
            len = PATH_FOLDER_JARS.length();
        } else if (isFolderRecursiveJarsPath(path)) {
            len = PATH_FOLDER_RECURSIVEJARS.length();
        } else {
            len = 0;
        }
        return path.substring(0, path.length() - len);
    }

    public static void main(String[] args) {
        System.out.println(processColonPath("$CM_HOME:a.jar,b.jar"));
        System.out.println(processColonPath("d:/others:a.jar,b.jar"));
        System.out.println(processColonPath("a.jar,b.jar"));
    }
}
