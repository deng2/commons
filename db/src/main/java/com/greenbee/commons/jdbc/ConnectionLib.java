// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import javax.sql.PooledConnection;
import javax.sql.XAConnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionLib {

    public static VendorBase getVendorImplementation(Connection conn)
            throws DBException, SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        String product = meta.getDatabaseProductName();
        if (VendorBase.ORACLE.equalsIgnoreCase(product)) {
            return new VendorOracle(meta);
        } else if (VendorBase.MYSQL.equalsIgnoreCase(product)) {
            return new VendorMySQL(meta);
        } else if (VendorBase.MSSQLSERVER.equalsIgnoreCase(product)) {
            return new VendorMSSQLServer(meta);
        } else {
            return new VendorBase(meta);
        }
    }

    public static Connection createConnection(String driverClazz, String url, String user,
            String pwd) throws SQLException, ClassNotFoundException {
        Class.forName(driverClazz);
        Connection conn = DriverManager.getConnection(url, user, pwd);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }

    public static Statement createScrollStatement(Connection conn) throws SQLException {
        return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    public static Statement createStatement(Connection conn) throws SQLException {
        return conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    public static PreparedStatement createPrepareStatement(Connection conn, String sql)
            throws SQLException {
        return conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException se) {
                //ignore
            }
        }
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException se) {
                //ignore
            }
        }

    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException se) {
                //ignore
            }
        }
    }

    public static void closePooledConnection(PooledConnection pooledConnection) {
        if (pooledConnection != null) {
            try {
                pooledConnection.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }

    public static void closeXAConnection(XAConnection xaConnection) {
        if (xaConnection != null) {
            try {
                xaConnection.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }
}
