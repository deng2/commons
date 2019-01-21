// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VendorMySQL extends VendorBase {

    public VendorMySQL(DatabaseMetaData meta) throws SQLException, DBException {
        super(meta);
    }

    @Override public Object[] getLastRow(RowMetaData rowMeta, Connection conn, String initialSQL)
            throws SQLException, DBException {
        Object[] lastRow = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = ConnectionLib.createStatement(conn);
            enableStreaming(statement, 1);
            rs = statement.executeQuery(initialSQL);
            while (rs.next()) {
                lastRow = getRow(rowMeta, rs);
            }
        } finally {
            ConnectionLib.closeResultSet(rs);
            ConnectionLib.closeStatement(statement);
        }
        return lastRow;
    }

    @Override public void enableStreaming(Statement stmt, int fetchSize) throws SQLException {
        stmt.setFetchSize(Integer.MIN_VALUE);//use streaming mode to avoid blow memory
    }

    @Override protected String getStatelessInitialTemplate() {
        return "SELECT %s FROM %s $$OPT$$";
    }

    @Override protected String getStatefulInitialTemplate() {
        return "SELECT %s FROM %s ORDER BY %s ASC $$OPT$$";
    }

    @Override protected String getRecurringTemplate() {
        return "SELECT %s FROM %s WHERE %s ORDER BY %s ASC $$OPT$$";
    }

    @Override protected String getOptStmt(int batchSize) {
        return "LIMIT " + String.valueOf(batchSize);
    }

}
