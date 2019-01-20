// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name_;
    private long lastModifiedTime_;
    private long size_;
    private String path_;

    public FileInfo(String fileDirPath, String fileName, long size, long mtime) {
        name_ = fileName;
        path_ = fileDirPath + "/" + fileName;
        size_ = size;
        lastModifiedTime_ = mtime;
    }

    /**
     * The file name like server.log without directory info
     */
    public String getName() {
        return name_;
    }

    /**
     * the file path like /home/logs/server.log
     */

    public String getPath() {
        return path_;
    }

    /**
     * The last modified time in milliseconds
     */
    public long getLastModifiedTime() {
        return lastModifiedTime_;
    }

    /**
     * The file size
     */
    public long getSize() {
        return size_;
    }

}
