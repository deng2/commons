// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public enum Type {
    BOOLEAN {
        @Override public String getXsdType() {
            return "boolean";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Boolean.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return false;
        }

    }, INTEGER {
        @Override public String getXsdType() {
            return "integer";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Integer.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return Integer.valueOf(0);
        }

    }, LONG {
        @Override public String getXsdType() {
            return "long";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Long.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return Long.valueOf(0L);
        }

    }, DECIMAL {
        @Override public String getXsdType() {
            return "double";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return BigDecimal.valueOf(Long.parseLong(literal));
        }

        @Override public Serializable getDefaultValue() {
            return BigDecimal.valueOf(0L);
        }

    }, FLOAT {
        @Override public String getXsdType() {
            return "float";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Float.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return Float.valueOf(0.0f);
        }

    }, DOUBLE {
        @Override public String getXsdType() {
            return "double";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Double.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return Double.valueOf(0.0d);
        }

    }, DATE {
        @Override public String getXsdType() {
            return "date";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Date.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return new Date(0L);
        }

    }, TIME {
        @Override public String getXsdType() {
            return "time";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Time.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return new Time(0L);
        }

    }, DATETIME {
        @Override public String getXsdType() {
            return "datetime";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return Timestamp.valueOf(literal);
        }

        @Override public Serializable getDefaultValue() {
            return new Timestamp(0L);
        }

    }, STRING {
        @Override public String getXsdType() {
            return "string";
        }

        @Override public Serializable convert(String literal) throws Exception {
            return literal;
        }

        @Override public Serializable getDefaultValue() {
            return "";
        }

    };

    public abstract String getXsdType();

    public abstract Serializable convert(String literal) throws Exception;

    public abstract Serializable getDefaultValue();
}
