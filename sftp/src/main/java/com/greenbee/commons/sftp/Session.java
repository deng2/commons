// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Session {
    /**
     * whether a such named file exist
     * return false if the directory with same name exists
     */
    public boolean hasFile(String filePath) throws SFTPException;

    /**
     * whether a such named directory exists
     */
    public boolean hasDir(String dirPath) throws SFTPException;

    /**
     * List all the files under this directory
     * Directories will be excluded
     */
    public List<FileInfo> ls(String dir) throws SFTPException;

    /**
     * remove a file on server and this API is not intended for removing directory
     */
    public void rm(String filePath) throws SFTPException;

    /**
     * 1. rename a file
     * 2. move a file to another directory
     */
    public void mv(String filePath, String newFilePath) throws SFTPException;

    /**
     * make a directory on server
     */
    public void mkdir(String dirPath) throws SFTPException;

    /**
     * remove a directory on server even when it is not empty
     */

    public void rmdir(String dirPath) throws SFTPException;

    /**
     * return the modified time of the file in milliseconds scale
     *
     */
    public long getFileModifiedTime(String filePath) throws SFTPException;

    /**
     * return the size of the file in byte scale
     */
    public long getFileSize(String filePath) throws SFTPException;

    /**
     * return the input stream of the specified file
     */
    public InputStream get(String fileName) throws SFTPException;

    /**
     * return the input stream of the specified with an offset
     */
    public InputStream get(String fileName, long skip) throws SFTPException;

    /**
     * return the output stream of a specified file and append the content to that file
     * @param append - true if append content to the end of the file 
     */
    public OutputStream put(String fileName, boolean append) throws SFTPException;

    /**
     * put a local file into SFTP server
     */

    public void put(String src, String dst) throws SFTPException;

    /**
     * close session to free resources
     */
    public void close();

}
