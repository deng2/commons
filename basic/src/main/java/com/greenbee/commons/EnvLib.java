// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons;

import com.greenbee.commons.regex.EnvVariableProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class EnvLib {
    public static final String ENV_PLACE_HOLDER = "\\$\\{([\\w:.]+)\\}|\\$([\\w]+)"; //like ${VTBA_HOME} or $VTBA_HOME
    private static final Log log_ = LogFactory.getLog(EnvLib.class);

    public static String getEnv(String name) {
        return System.getenv(name);
    }

    /**
     * expand the environment variables in the path
     */
    public static String expandVars(String classpath) {
        EnvVariableProcessor processor = new EnvVariableProcessor(classpath);
        List<String> names = processor.getPlaceHolderNames();
        HashMap<String, String> values = new HashMap<String, String>();
        for (String name : names) {
            String var = EnvLib.getEnv(name);
            if (!StringUtils.isEmpty(var)) {
                values.put(name, var);
            } else {
                if (log_.isWarnEnabled()) {
                    log_.warn(String.format("Environment variable %s is missing", name));
                }
            }
        }
        return processor.replace(values);
    }

    public static String expandVars(String classpath, Properties env) {
        EnvVariableProcessor processor = new EnvVariableProcessor(classpath);
        List<String> names = processor.getPlaceHolderNames();
        HashMap<String, String> values = new HashMap<String, String>();
        for (String name : names) {
            String var = env.getProperty(name);
            if (!StringUtils.isEmpty(var)) {
                values.put(name, var);
            }
        }
        return processor.replace(values);
    }

    public static void main(String[] args) {
        System.out.println(expandVars("$VTBA_HOME/jboss"));
        System.out.println(expandVars("${VTBA_HOME}/jboss"));
        System.out.println(expandVars("${VTNONE_EXIST:10.111.3.66}/jboss"));
    }
}
