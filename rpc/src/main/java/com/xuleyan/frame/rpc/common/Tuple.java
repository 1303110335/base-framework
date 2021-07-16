/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.common;

import java.lang.reflect.Method;

/**
 *
 * @author xuleyan
 * @version Tuple.java, v 0.1 2021-07-08 9:17 下午
 */
public class Tuple {
    private Object instance;
    private Method method;

    public Tuple(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    public Object getinstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }
}
