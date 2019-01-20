// Copyright (c) 2016 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.javascript;

import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;

public class ScriptLib {
    public static Object unwrap(Object object) {
        if (object instanceof Wrapper)
            object = ((Wrapper) object).unwrap();
        return (object instanceof Undefined) ? null : object;
    }
}
