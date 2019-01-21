// Copyright (c) 2013 by Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import java.sql.Connection;

public interface DBOperation<T> {

    T operate(Connection conn) throws Exception;

}