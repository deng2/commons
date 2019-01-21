// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

public class VendorOracle extends VendorBase {

    protected static final String SQLTYPE_NUMBER = "NUMBER";

    public VendorOracle(DatabaseMetaData meta) throws SQLException, DBException {
        super(meta);
    }

    @Override
    protected int getColumnSQLType(ResultSetMetaData rsmd, int i)
            throws SQLException, DBException {
        int sqlType = rsmd.getColumnType(i);
        String sqlTypeName = rsmd.getColumnTypeName(i);
        if (sqlType == -101 || sqlType == -102) {
            //For data type: TIMESTAMP WITH LOCAL TIME ZONE  and TIMESTAMP WITH TIME ZONE
            return Types.TIMESTAMP;
        }
        if (SQLTYPE_NUMBER.equalsIgnoreCase(sqlTypeName)) {
            int precision = rsmd.getPrecision(i);
            int scale = rsmd.getScale(i);
            if (precision > 0 && scale == 0) {//exclude decimal with points
                if (precision == 1) {//boolean type
                    return Types.BIT;
                } else if (precision <= 10) {//integer type
                    return Types.INTEGER;
                } else if (precision <= 19) {//long type
                    return Types.BIGINT;
                }
                //decimal type
            }
        }
        return super.getColumnSQLType(rsmd, i);
    }

}
