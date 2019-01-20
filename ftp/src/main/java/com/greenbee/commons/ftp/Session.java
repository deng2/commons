// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.ftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface Session {

    public void enterNoopMode() throws FTPException;

    public void leaveNoopMode() throws FTPException;

    /**
     * whether a such named file exist
     * return false if the directory with same name exists
     */
    public boolean hasFile(String filePath) throws FTPException;

    /**
     * whether a such named directory exists
     */
    public boolean hasDir(String dirPath) throws FTPException;

    /**
     * List all the files under this directory
     * Directories will be excluded
     */
    public List<FileInfo> ls(String dirPath) throws FTPException;

    /**
     * remove a file on server and this API is not intended for removing directory
     */
    public void rm(String filePath) throws FTPException;

    /**
     * 1. rename a file
     * 2. move a file to another directory
     */
    public void mv(String filePath, String newFilePath) throws FTPException;

    /**
     * make a directory on server
     */
    public void mkdir(String dirPath) throws FTPException;

    /**
     * remove a directory on server even when it is not empty
     */

    public void rmdir(String dirPath) throws FTPException;

    /**
     * return the modified time of the file in milliseconds scale
     *
     */
    public long getFileModifiedTime(String filePath) throws FTPException;

    /**
     * return the size of the file in byte scale
     */
    public long getFileSize(String filePath) throws FTPException;

    /**
     * return the input stream of the specified file
     */
    public InputStream get(String filePath) throws FTPException;

    /**
     * return the input stream of the specified with an offset
     */
    public InputStream get(String filePath, long skip) throws FTPException;

    /**
     * return the output stream of a specified file and append the content to that file
     * @param append - true if append content to the end of the file 
     */
    public OutputStream put(String filePath, boolean append) throws FTPException;

    public OutputStream put(String filePath) throws FTPException;

    public void put(String filePath, boolean append, String localFileName) throws FTPException;

    public void put(String filePath, String localFileName) throws FTPException;

    /**
     * close session to free resources
     */
    public void close();

}
