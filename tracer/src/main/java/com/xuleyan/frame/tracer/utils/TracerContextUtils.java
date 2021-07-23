/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.utils;

import org.slf4j.MDC;

/**
 *
 * @author xuleyan
 * @version TracerContextUtils.java, v 0.1 2021-07-23 3:23 下午
 */
public class TracerContextUtils {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setTracerId(String tracerId) {
        MDC.put("traceId", tracerId);
        threadLocal.set(tracerId);
    }

    public static void clear() {
        threadLocal.remove();
        MDC.clear();
    }
}