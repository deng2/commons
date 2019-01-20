package com.greenbee.commons.javascript;

import org.mozilla.javascript.ImporterTopLevel;

public class ScriptScope extends ImporterTopLevel {
    private static final long serialVersionUID = 1L;

    protected ScriptScope(GlobalScope global) {
        super();
        setPrototype(global);
        cacheBuiltins();
        exportAsJSClass(3, this, false);
        delete("constructor");
    }

    public void put(String name, Object value) {
        put(name, this, value);
    }

    public String getClassName() {
        return "Script";
    }

}
