/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.inteceptor;

import com.xuleyan.frame.tracer.configuration.TracerProperties;
import org.aopalliance.intercept.MethodInterceptor;

/**
 *
 * @author xuleyan
 * @version TracingMethodInterceptor.java, v 0.1 2021-07-24 1:30 下午
 */
public abstract class TracingMethodInterceptor implements MethodInterceptor {

    protected final TracerProperties tracerProperties;

    public TracingMethodInterceptor(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
    }
}