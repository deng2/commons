// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import com.greenbee.commons.StringUtils;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

public class VendorBase {
    public static final String MYSQL = "MySQL";
    public static final String ORACLE = "Oracle";
    public static final String MSSQLSERVER = "Microsoft SQL Server";

    public VendorBase(DatabaseMetaData meta) throws SQLException, DBException {
    }

    protected String getColumnName(ResultSetMetaData rsmd, int i)
            throws SQLException, DBException {//always convert to lower case
        String name = rsmd.getColumnLabel(i);
        if (StringUtils.isEmpty(name))
            name = rsmd.getColumnName(i);
        if (StringUtils.isEmpty(name))
            name = "column" + i;
        return name.toLowerCase();
    }

    protected Type getColumnType(ResultSetMetaData rsmd, int i) throws SQLException, DBException {
        int sqlType = getColumnSQLType(rsmd, i);
        Type type = null;
        switch (sqlType) {
        case Types.BIT:
        case Types.BOOLEAN:
            type = Type.BOOLEAN;
            break;
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
            type = Type.INTEGER;
            break;
        case Types.BIGINT:
        case Types.ROWID:
            type = Type.LONG;
            break;
        case Types.DECIMAL:
        case Types.NUMERIC:
            type = Type.DECIMAL;
            break;
        case Types.REAL:
        case Types.FLOAT:
            type = Type.FLOAT;
            break;
        case Types.DOUBLE:
            type = Type.DOUBLE;
            break;
        case Types.TIME:
            type = Type.TIME;
            break;
        case Types.DATE:
            type = Type.DATE;
            break;
        case Types.TIMESTAMP:
            type = Type.DATETIME;
            break;
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.NCHAR:
        case Types.NVARCHAR:
        case Types.LONGNVARCHAR:
            type = Type.STRING;
            break;
        case Types.CLOB:
        case Types.NCLOB:
            type = Type.STRING;
            break;
        default:
            String msg = String.format("The column %s sql type %s is not supported", i, sqlType);
            throw new DBException(msg);
        }
        return type;
    }

    protected int getColumnSQLType(ResultSetMetaData rsmd, int i) throws SQLException, DBException {
        return rsmd.getColumnType(i);
    }

    protected Object getColumnValue(ResultSet rs, int i, int sqlType)
            throws SQLException, DBException {
        Object value;
        switch (sqlType) {
        case Types.BIT:
        case Types.BOOLEAN:
            value = rs.getBoolean(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.TINYINT:
        case Types.SMALLINT:
        case Types.INTEGER:
            value = rs.getInt(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.BIGINT:
        case Types.ROWID:
            value = rs.getLong(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.DECIMAL:
        case Types.NUMERIC:
            value = rs.getBigDecimal(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.REAL:
        case Types.FLOAT:
            value = rs.getFloat(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.DOUBLE:
            value = rs.getDouble(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.TIME:
            value = rs.getTime(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.DATE:
            value = rs.getDate(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.TIMESTAMP:
            value = rs.getTimestamp(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.NCHAR:
        case Types.NVARCHAR:
        case Types.LONGNVARCHAR:
            value = rs.getString(i);
            if (rs.wasNull())
                value = null;
            break;
        case Types.CLOB:
        case Types.NCLOB:
            Clob clob = rs.getClob(i);
            if (clob == null) {
                value = null;
            } else {
                int len = (int) clob.length();
                value = clob.getSubString(1, len);
            }
            break;
        default:
            String msg = String.format("The column %s sql type %s is not supported", i, sqlType);
            throw new DBException(msg);
        }
        return value;
    }

    public RowMetaData getMetaData(ResultSetMetaData rsmd) throws SQLException, DBException {
        int count = rsmd.getColumnCount();
        String[] names = new String[count];
        Type[] types = new Type[count];
        int[] sqlTypes = new int[count];
        for (int i = 0; i < count; i++) {
            names[i] = getColumnName(rsmd, i + 1);
            types[i] = getColumnType(rsmd, i + 1);
            sqlTypes[i] = getColumnSQLType(rsmd, i + 1);
        }
        return new RowMetaData(names, types, sqlTypes);
    }

    public Object[] getRow(RowMetaData metaData, ResultSet rs) throws SQLException, DBException {
        int count = metaData.getCount();
        Object[] values = new Object[count];
        for (int i = 0; i < count; i++) {
            values[i] = getColumnValue(rs, i + 1, metaData.getSQLType(i));
        }
        return values;
    }

    public Object[] getLastRow(RowMetaData rowMeta, Connection conn, String initialSQL)
            throws SQLException, DBException {
        Object[] lastRow = null;
        Statement statement = null;
        ResultSet rs = null;//get initial state from last row
        try {
            statement = ConnectionLib.createScrollStatement(conn);
            statement.setFetchSize(1);
            rs = statement.executeQuery(initialSQL);
            if (rs.last()) {
                lastRow = getRow(rowMeta, rs);
            }
        } finally {
            ConnectionLib.closeResultSet(rs);
            ConnectionLib.closeStatement(statement);
        }
        return lastRow;
    }

    public void enableStreaming(Statement stmt, int fetchSize) throws SQLException {
        stmt.setFetchSize(fetchSize);
    }

    public String buildInitialSQL(String tableName, List<String> columns,
            List<String> stateColumns) {
        if (stateColumns == null || stateColumns.size() == 0) {
            String msg = getStatelessInitialTemplate();
            return String.format(msg, getColumnNames(columns), tableName);
        } else {
            String msg = getStatefulInitialTemplate();
            return String
                    .format(msg, getColumnNames(columns), tableName, getColumnNames(stateColumns));
        }
    }

    public String buildRecurringSQL(String tableName, List<String> columns,
            List<String> stateColumns) {
        String msg = getRecurringTemplate();
        return String.format(msg, getColumnNames(columns), tableName, getWhereClause(stateColumns),
                getColumnNames(stateColumns));
    }

    public String getOptimizedInitSQL(String osql, int fetchSize) {
        return getOptimizedRecurringSQL(osql, fetchSize);
    }

    public String getNoneOptimizedInitSQL(String osql) {
        return getOptimizedRecurringSQL(osql, -1);
    }

    public String getOptimizedRecurringSQL(String osql, int fetchSize) {
        if (fetchSize <= 0) {
            return osql.replace("$$OPT$$", "");
        } else {
            return osql.replace("$$OPT$$", getOptStmt(fetchSize));
        }
    }

    protected String getColumnNames(List<String> columns) {
        StringBuilder sb = new StringBuilder(1024);
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(columns.get(i));
        }
        return sb.toString();//columneA, columnB, columnC
    }

    protected String getWhereClause(List<String> columns) {
        StringBuilder sb = new StringBuilder(1024);
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(" AND ");
            }
            String column = columns.get(i);
            String relation;
            if (i < size - 1) {
                relation = " >= ";
            } else {
                relation = " > ";
            }
            sb.append(column).append(relation).append("$$").append(column).append("$$");
        }
        return sb
                .toString();//columnA >= $$columnA$$ AND columnB >= $$columnB$$ AND columnC > $$columnC$$
    }

    protected String getStatelessInitialTemplate() {
        return "SELECT %s FROM %s";
    }

    protected String getStatefulInitialTemplate() {
        return "SELECT %s FROM %s ORDER BY %s ASC";
    }

    protected String getRecurringTemplate() {
        return "SELECT %s FROM %s WHERE %s ORDER BY %s ASC";
    }

    protected String getOptStmt(int batchSize) {
        return "";
    }
}
