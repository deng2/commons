// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.ftp;

public class ConnectionInfo {
    public static final String AUTH_TYPE_PASSWORD = "PASSWORD";
    public static final String AUTH_TYPE_KEY = "KEY";
    public static final String PROXY_TYPE_NONE = "NONE";
    //public static final String PROXY_TYPE_SOCKS4 = "SOCKS4";
    public static final String PROXY_TYPE_SOCKS5 = "SOCKS5";

    private String serverHost_;
    private int serverPort_ = 22;
    private String user_;
    private String authType_ = AUTH_TYPE_PASSWORD;
    private String password_;
    private String privateKeyFile_;
    private String passphrase_;

    private String proxyType_ = PROXY_TYPE_NONE;
    private String proxyHost_;
    private int proxyPort_ = 1080;
    private String proxyUser_;
    private String proxyPassword_;

    private int timeout_ = -1;

    public String getServerHost() {
        return serverHost_;
    }

    public void setServerHost(String serverHost) {
        this.serverHost_ = serverHost;
    }

    public int getServerPort() {
        return serverPort_;
    }

    public void setServerPort(int serverPort) {
        this.serverPort_ = serverPort;
    }

    public String getUser() {
        return user_;
    }

    public void setUser(String user) {
        this.user_ = user;
    }

    public String getAuthType() {
        return authType_;
    }

    public void setAuthType(String authType) {
        this.authType_ = authType;
    }

    public String getPassword() {
        return password_;
    }

    public void setPassword(String password) {
        this.password_ = password;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile_;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile_ = privateKeyFile;
    }

    public String getPassphrase() {
        return passphrase_;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase_ = passphrase;
    }

    public String getProxyType() {
        return proxyType_;
    }

    public void setProxyType(String proxyType) {
        this.proxyType_ = proxyType;
    }

    public String getProxyHost() {
        return proxyHost_;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost_ = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort_;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort_ = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser_;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser_ = proxyUser;
    }

    public String getProxyPassword() {
        return proxyPassword_;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword_ = proxyPassword;
    }

    public int getSocketTimeout() {
        return timeout_;
    }

    public void setSocketTimeout(int timeout) {
        this.timeout_ = timeout;
    }
}
