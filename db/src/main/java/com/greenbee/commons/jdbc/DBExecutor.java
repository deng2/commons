// Copyright (c) 2013 by Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBExecutor {
    private DataSource ds_;

    public DBExecutor(DataSource ds) {
        this.ds_ = ds;
    }

    public <T> T execute(DBOperation<T> opt) throws Exception {
        Connection conn = null;
        try {
            Exception exception = null;
            T result = null;
            conn = ds_.getConnection();
            conn.setAutoCommit(false);
            try {
                result = opt.operate(conn);
            } catch (Exception e) {
                exception = e;
            }
            if (exception == null) {
                conn.commit();
            } else {
                try {
                    conn.rollback();
                } catch (SQLException se) {
                    //ignore and throw the real exception                
                }
                throw exception;
            }
            return result;
        } finally {
            ConnectionLib.closeConnection(conn);
        }
    }
}
