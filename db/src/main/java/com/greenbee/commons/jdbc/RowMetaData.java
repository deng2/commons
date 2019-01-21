// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

public class RowMetaData {
    private String[] names_;
    private Type[] types_;
    private int[] sqlTypes_;

    public RowMetaData(String[] names, Type[] types, int[] sqlTypes) throws DBException {
        //check if the column names are unique
        for (int i = 0; i < names.length; i++) {
            for (int j = i + 1; j < names.length; j++) {
                if (names[i].equals(names[j])) {
                    String msg = String
                            .format("The column %s and %s has same column name %s", i + 1, j + 1,
                                    names[i]);
                    throw new DBException(msg);
                }
            }
        }
        names_ = new String[names.length];
        System.arraycopy(names, 0, names_, 0, names.length);
        types_ = new Type[types.length];
        System.arraycopy(types, 0, types_, 0, types.length);
        sqlTypes_ = new int[sqlTypes.length];
        System.arraycopy(sqlTypes, 0, sqlTypes_, 0, sqlTypes.length);
    }

    public int getCount() {
        return types_.length;
    }

    public String getName(int i) {
        return names_[i];
    }

    public Type getType(int i) {
        return types_[i];
    }

    public int getSQLType(int i) {
        return sqlTypes_[i];
    }

}
