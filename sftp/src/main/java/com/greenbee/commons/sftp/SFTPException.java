// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp;

public class SFTPException extends Exception {

    private static final long serialVersionUID = 1L;

    public SFTPException() {
    }

    public SFTPException(String message) {
        super(message);
    }

    public SFTPException(Throwable cause) {
        super(cause);
    }

    public SFTPException(String message, Throwable cause) {
        super(message, cause);
    }

}
