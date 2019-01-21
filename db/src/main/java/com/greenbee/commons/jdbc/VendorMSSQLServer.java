// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class VendorMSSQLServer extends VendorBase {

    public VendorMSSQLServer(DatabaseMetaData meta) throws SQLException, DBException {
        super(meta);
    }

    @Override protected String getStatelessInitialTemplate() {
        return "SELECT $$OPT$$ %s FROM %s";
    }

    @Override protected String getStatefulInitialTemplate() {
        return "SELECT $$OPT$$ %s FROM %s ORDER BY %s ASC";
    }

    @Override protected String getRecurringTemplate() {
        return "SELECT $$OPT$$ %s FROM %s WHERE %s ORDER BY %s ASC";
    }

    @Override protected String getOptStmt(int batchSize) {
        return "TOP " + String.valueOf(batchSize);
    }

}
