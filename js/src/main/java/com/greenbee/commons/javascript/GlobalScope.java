package com.greenbee.commons.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.TopLevel;

public class GlobalScope extends TopLevel {
    private static final long serialVersionUID = 1L;

    protected GlobalScope(Context context) {
        context.initStandardObjects(this);
        Scriptable packages = (Scriptable) get("Packages", this);
        this.put("com", this, packages.get("com", packages));
        this.put("edu", this, packages.get("edu", packages));
        this.put("javax", this, packages.get("javax", packages));
        this.put("net", this, packages.get("net", packages));
        this.put("org", this, packages.get("org", packages));
        String[] funcs = new String[] { "println", "print" };
        defineFunctionProperties(funcs, Function.class, ScriptableObject.DONTENUM);
    }

    public String getClassName() {
        return "Global";
    }

}
