// Copyright (c) 2013 by Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.hibernate;

import org.hibernate.Session;

public interface SessionOperation<T> {

    T operate(Session session) throws Exception;

}
