// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

public class XmlLib {
    private static DocumentBuilderFactory dbf_ = null;
    private static TransformerFactory tf_ = null;

    public static synchronized DocumentBuilderFactory getDocumentBuilderFactory() {
        if (dbf_ == null) {
            dbf_ = DocumentBuilderFactory.newInstance();
            dbf_.setXIncludeAware(false);
            dbf_.setValidating(false);
            dbf_.setIgnoringComments(true);
            //dbf_.setIgnoringElementContentWhitespace(true);
            dbf_.setNamespaceAware(true);
        }
        return dbf_;
    }

    public static synchronized TransformerFactory getTransformerFactory() {
        if (tf_ == null) {
            tf_ = TransformerFactory.newInstance();
            //tf_.setAttribute("{http://xml.apache.org/xslt}indent-amount", 4);
        }
        return tf_;
    }
}
