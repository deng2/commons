// Copyright (c) 2016 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.regex;

public class EnvVariableProcessor extends PlaceHolderProcessor {

    public EnvVariableProcessor(String str) {
        super(str, "%", "%", "${", "}", "$");
    }

    public static void main(String[] args) {
        EnvVariableProcessor evp = new EnvVariableProcessor("%VT_H%/${A_H}/$B_H");
        System.out.println(evp.getPlaceHolderNames());
    }
}
