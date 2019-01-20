// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp.impl;

import com.jcraft.jsch.UserInfo;

public class SFTPUserInfo implements UserInfo {

    public String getPassphrase() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public boolean promptPassword(String string) {
        return false;
    }

    public boolean promptPassphrase(String string) {
        return false;
    }

    //always add host to known host
    public boolean promptYesNo(String string) {
        return true;
    }

    public void showMessage(String string) {

    }

}
