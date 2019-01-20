// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.ftp.impl;

import com.greenbee.commons.ftp.ConnectionInfo;
import com.greenbee.commons.ftp.ConnectionLib;
import com.greenbee.commons.ftp.FTPException;
import com.greenbee.commons.ftp.FileInfo;
import com.greenbee.commons.ftp.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SessionImpl implements Session {
    private static final Log log_ = LogFactory.getLog(SessionImpl.class);
    private ConnectionInfo info_;
    private FTPClient fc_;

    private ScheduledExecutorService scheduler_ = null;
    private ScheduledFuture<?> furture_;

    protected SessionImpl(SessionFactoryImpl sf) throws FTPException {
        try {
            info_ = sf.getConnectionInfo();
            fc_ = new FTPClient();
            PrintWriter pw = new LogPrintWriter();
            PrintCommandListener listener = new PrintCommandListener(pw, true);
            fc_.addProtocolCommandListener(listener);
            fc_.setControlEncoding("utf-8");
            fc_.connect(info_.getServerHost(), info_.getServerPort());
            if (!FTPReply.isPositiveCompletion(fc_.getReplyCode()))
                throw new FTPException("Fail to connect");
            //fc_.enterLocalPassiveMode();
            boolean success = fc_.login(info_.getUser(), info_.getPassword());
            if (!success)
                throw new FTPException("Fail to login");
            fc_.setFileType(FTP.BINARY_FILE_TYPE);
            fc_.enterLocalPassiveMode();
        } catch (Exception e) {
            //do cleanup
            close();
            throw wrapException(e);
        }
    }

    protected ScheduledExecutorService getScheduler() {
        if (scheduler_ == null) {
            scheduler_ = Executors.newScheduledThreadPool(1);
        }
        return scheduler_;
    }

    @Override public void enterNoopMode() throws FTPException {
        if (furture_ != null)
            return;
        furture_ = getScheduler().scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                try {
                    fc_.noop();
                } catch (Exception e) {
                    log_.error("Exception happens during heartbeat to FTP server", e);
                }
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    @Override public void leaveNoopMode() {
        if (furture_ != null) {
            furture_.cancel(false);
            furture_ = null;
        }
    }

    @Override public boolean hasDir(String dirPath) throws FTPException {
        try {
            FTPFile file = fc_.mlistFile(dirPath);
            return file != null && file.isDirectory();
        } catch (IOException ioe) {
            throw wrapException(ioe);
        }
    }

    @Override public boolean hasFile(String filePath) throws FTPException {
        try {
            FTPFile file = fc_.mlistFile(filePath);
            return file != null && file.isFile();
        } catch (IOException ioe) {
            throw wrapException(ioe);
        }
    }

    @Override public List<FileInfo> ls(String dirPath) throws FTPException {
        try {
            List<FTPFile> files = Arrays.asList(fc_.mlistDir(dirPath));
            ArrayList<FileInfo> rtn = new ArrayList<FileInfo>(files.size());
            for (FTPFile file : files) {
                if (!file.isFile())
                    continue;
                String fileName = file.getName();
                FileInfo info = new FileInfo(dirPath, fileName, file.getSize(),
                        file.getTimestamp().getTimeInMillis());
                rtn.add(info);
            }
            return rtn;
        } catch (IOException se) {
            throw wrapException(se);
        }
    }

    @Override public void rm(String filePath) throws FTPException {
        try {
            if (!fc_.deleteFile(filePath))
                throw new FTPException("Fail to remove");
        } catch (IOException ioe) {
            throw wrapException(ioe);
        }
    }

    @Override public void mv(String filePath, String newFilePath) throws FTPException {
        try {
            if (!fc_.rename(filePath, newFilePath)) {
                throw new FTPException("Fail to move/rename");
            }
        } catch (IOException ioe) {
            throw wrapException(ioe);
        }
    }

    @Override public void mkdir(String dirPath) throws FTPException {
        try {
            if (!fc_.makeDirectory(dirPath)) {
                throw new FTPException("Fail to make dir");
            }
        } catch (IOException ioe) {
            throw wrapException(ioe);
        }
    }

    @Override public void rmdir(String dirPath) throws FTPException {
        try {
            List<FTPFile> files = Arrays.asList(fc_.mlistDir(dirPath));
            for (FTPFile file : files) {
                String name = file.getName();
                if (".".equals(name) || "..".equals(name)) //ignore path . and ..
                    continue;
                String path = dirPath + "/" + name;
                if (file.isDirectory())
                    rmdir(path);//remove directories
                else
                    fc_.deleteFile(path);//remove files
            }
            fc_.removeDirectory(dirPath);
        } catch (IOException ioe) {
            throw wrapException(ioe);
        }
    }

    @Override public long getFileModifiedTime(String filePath) throws FTPException {
        try {
            FTPFile file = fc_.mlistFile(filePath);
            if (file == null)
                throw new FTPException("File not exist");
            return file.getTimestamp().getTimeInMillis();
        } catch (IOException se) {
            throw wrapException(se);
        }
    }

    @Override public long getFileSize(String filePath) throws FTPException {
        try {
            FTPFile file = fc_.mlistFile(filePath);
            if (file == null)
                throw new FTPException("File not exist");
            return file.getSize();
        } catch (IOException se) {
            throw wrapException(se);
        }
    }

    @Override public InputStream get(String filePath) throws FTPException {
        return get(filePath, 0L);
    }

    @Override public InputStream get(String filePath, long skip) throws FTPException {
        try {
            fc_.setRestartOffset(skip);
            InputStream is = fc_.retrieveFileStream(filePath);
            int code = fc_.getReplyCode();
            if (!FTPReply.isPositiveIntermediate(code) && !FTPReply.isPositivePreliminary(code)) {
                throw new FTPException("Fail to get");
            }
            return new InputStreamWrapper(fc_, is);
        } catch (IOException se) {
            throw wrapException(se);
        }
    }

    @Override public OutputStream put(String filePath, boolean append) throws FTPException {
        try {
            OutputStream os;
            if (append)
                os = fc_.appendFileStream(filePath);
            else
                os = fc_.storeFileStream(filePath);
            int code = fc_.getReplyCode();
            if (!FTPReply.isPositiveIntermediate(code) && !FTPReply.isPositivePreliminary(code)) {
                throw new FTPException("Fail to put");
            }
            return new OutputStreamWrapper(fc_, os);
        } catch (IOException se) {
            throw wrapException(se);
        }
    }

    public OutputStream put(String filePath) throws FTPException {
        return put(filePath, false);

    }

    public void put(String filePath, boolean append, String localFileName) throws FTPException {
        InputStream is = null;
        try {
            is = new FileInputStream(localFileName);
            if (append) {
                fc_.appendFile(filePath, is);
            } else {
                fc_.storeFile(filePath, is);
            }
        } catch (Exception e) {
            throw wrapException(e);
        } finally {
            ConnectionLib.closeStream(is);
        }
    }

    public void put(String filePath, String localFileName) throws FTPException {
        put(filePath, false, localFileName);
    }

    @Override public void close() {
        if (fc_ != null && fc_.isConnected()) {
            try {
                fc_.disconnect();
            } catch (IOException ioe) {
                //ignore
            }
        }
        fc_ = null;
        if (scheduler_ != null) {
            scheduler_.shutdown();
            try {
                scheduler_.awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException ie) {
                //ignore
                log_.error("Fail to shutdown executor service", ie);
            }
            scheduler_ = null;
        }

    }

    protected FTPException wrapException(Exception e) {
        if (e instanceof FTPException)
            return (FTPException) e;
        return new FTPException(e);
    }

}
