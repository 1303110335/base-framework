/**
 * bianque.com
 * Copyright (C) 2013-2021All Rights Reserved.
 */
package com.xuleyan.frame.rpc.key;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author xuleyan
 * @version RpcMethod.java, v 0.1 2021-07-08 9:22 下午
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcMethod {

    /**
     * 服务key
     * @return
     */
    String key();
}