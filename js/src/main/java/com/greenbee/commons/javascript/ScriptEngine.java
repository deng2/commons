package com.greenbee.commons.javascript;

import org.mozilla.javascript.Context;

public class ScriptEngine {
    protected GlobalScope globalScope_;

    public ScriptEngine() {
        Context context = Context.enter();
        try {
            globalScope_ = new GlobalScope(context);
        } finally {
            Context.exit();
        }
    }

    public ScriptScope newScriptScope() {
        return new ScriptScope(globalScope_);
    }

    public Object eval(ScriptScope scriptScope, String script) throws Exception {
        Context context = Context.enter();
        try {
            context.getWrapFactory().setJavaPrimitiveWrap(false);
            scriptScope.setPrototype(globalScope_);
            Object result = context.evaluateString(scriptScope, script, "script-engine", 1, null);
            return ScriptLib.unwrap(result);
        } finally {
            Context.exit();
        }
    }

}
