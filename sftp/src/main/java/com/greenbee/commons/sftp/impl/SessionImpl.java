// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp.impl;

import com.greenbee.commons.sftp.ConnectionInfo;
import com.greenbee.commons.sftp.FileInfo;
import com.greenbee.commons.sftp.SFTPException;
import com.greenbee.commons.sftp.Session;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SessionImpl implements Session {
    private ConnectionInfo info_;
    private JSch jsch_ = null;
    private com.jcraft.jsch.Session session_ = null;
    private ChannelSftp channel_ = null;

    protected SessionImpl(SessionFactoryImpl sf) throws SFTPException {
        try {
            info_ = sf.getConnectionInfo();
            int timeout = info_.getSocketTimeout();
            jsch_ = new JSch();
            //key authentication
            if (ConnectionInfo.AUTH_TYPE_KEY.equals(info_.getAuthType())) {
                jsch_.addIdentity(info_.getPrivateKeyFile(), info_.getPassphrase());
            }

            session_ = jsch_
                    .getSession(info_.getUser(), info_.getServerHost(), info_.getServerPort());
            //password authentication
            if (ConnectionInfo.AUTH_TYPE_PASSWORD.equals(info_.getAuthType())) {
                session_.setPassword(info_.getPassword());
            }
            session_.setUserInfo(new SFTPUserInfo());
            //PROXY server support
            if (ConnectionInfo.PROXY_TYPE_SOCKS5.equals(info_.getProxyType())) {
                ProxySOCKS5 proxy = new ProxySOCKS5(info_.getProxyHost(), info_.getProxyPort());
                proxy.setUserPasswd(info_.getProxyUser(), info_.getProxyPassword());
                session_.setProxy(proxy);
            }
            if (timeout <= 0) {
                session_.connect();
            } else {
                session_.connect(timeout * 1000);
            }

            channel_ = (ChannelSftp) session_.openChannel("sftp");
            if (timeout <= 0) {
                channel_.connect();
            } else {
                channel_.connect(timeout * 1000);
            }
        } catch (JSchException jse) {
            //do cleanup
            close();
            throw new SFTPException(jse);
        }
    }

    @Override public boolean hasDir(String dirPath) throws SFTPException {
        try {
            SftpATTRS stat = channel_.stat(dirPath);
            return stat.isDir();
        } catch (SftpException se) {
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == se.id)
                return false;
            else
                throw wrapException(se);
        }
    }

    @Override public boolean hasFile(String filePath) throws SFTPException {
        try {
            SftpATTRS stat = channel_.stat(filePath);
            return !stat.isDir();
        } catch (SftpException se) {
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE == se.id)
                return false;
            else
                throw wrapException(se);
        }
    }

    @SuppressWarnings("unchecked") @Override public List<FileInfo> ls(String dir)
            throws SFTPException {
        try {
            Vector<LsEntry> entries = channel_.ls(dir);
            ArrayList<FileInfo> rtn = new ArrayList<FileInfo>(entries.size());
            for (LsEntry entry : entries) {
                SftpATTRS attr = entry.getAttrs();
                if (attr.isDir())
                    continue;
                String fileName = entry.getFilename();
                FileInfo info = new FileInfo(dir, fileName, attr.getSize(),
                        attr.getMTime() * 1000L);
                rtn.add(info);
            }
            return rtn;
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public void rm(String filePath) throws SFTPException {
        try {
            channel_.rm(filePath);
        } catch (SftpException se) {
            if (ChannelSftp.SSH_FX_NO_SUCH_FILE != se.id) {
                throw wrapException(se);
            }
        }
    }

    @Override public void mv(String filePath, String newFilePath) throws SFTPException {
        try {
            channel_.rename(filePath, newFilePath);
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public void mkdir(String dirPath) throws SFTPException {
        try {
            channel_.mkdir(dirPath);
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @SuppressWarnings("unchecked") @Override public void rmdir(String dirPath)
            throws SFTPException {
        try {
            Vector<LsEntry> entries = channel_.ls(dirPath);
            for (LsEntry entry : entries) {
                SftpATTRS attr = entry.getAttrs();
                String name = entry.getFilename();
                if (".".equals(name) || "..".equals(name)) //ignore path . and ..
                    continue;
                String path = dirPath + "/" + name;
                if (attr.isDir())
                    rmdir(path);//remove directories
                else
                    channel_.rm(path);//remove files
            }
            channel_.rmdir(dirPath);
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public long getFileModifiedTime(String filePath) throws SFTPException {
        try {
            SftpATTRS stat = channel_.stat(filePath);
            return stat.getMTime() * 1000L;
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public long getFileSize(String filePath) throws SFTPException {
        try {
            SftpATTRS stat = channel_.stat(filePath);
            return stat.getSize();
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public InputStream get(String fileName) throws SFTPException {
        return get(fileName, 0L);
    }

    @Override public InputStream get(String fileName, long skip) throws SFTPException {
        try {
            return channel_.get(fileName, (SftpProgressMonitor) null, skip);
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public OutputStream put(String fileName, boolean append) throws SFTPException {
        try {
            int mode = append ? ChannelSftp.APPEND : ChannelSftp.OVERWRITE;
            return channel_.put(fileName, mode);
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public void put(String src, String dst) throws SFTPException {
        try {
            channel_.put(src, dst);
        } catch (SftpException se) {
            throw wrapException(se);
        }
    }

    @Override public void close() {
        if (channel_ != null) {
            channel_.disconnect();
            channel_ = null;
        }
        if (session_ != null) {
            session_.disconnect();
            session_ = null;
        }
        jsch_ = null;
    }

    protected SFTPException wrapException(SftpException se) throws SFTPException {
        return new SFTPException(se);
    }

}
