// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.ftp;

public class FTPException extends Exception {

    private static final long serialVersionUID = 1L;

    public FTPException() {
    }

    public FTPException(String message) {
        super(message);
    }

    public FTPException(Throwable cause) {
        super(cause);
    }

    public FTPException(String message, Throwable cause) {
        super(message, cause);
    }

}
