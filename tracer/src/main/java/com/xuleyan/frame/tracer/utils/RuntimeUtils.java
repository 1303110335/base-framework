/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.utils;

import java.lang.management.ManagementFactory;

/**
 *
 * @author xuleyan
 * @version RuntimeUtils.java, v 0.1 2021-07-23 3:34 下午
 */
public class RuntimeUtils {

    private RuntimeUtils() {
    }

    private final static int pid = pid();

    public static int getPid() {
        return pid;
    }

    /**
     * 获得当前进程的PID
     * 当失败时返回0
     */
    private static int pid() {
        /* format: "pid@hostname" */
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        String[] split = jvmName.split("@");
        if (split.length != 2) {
            return 0;
        }
        try {
            return Integer.parseInt(split[0]);
        } catch (Exception e) {
            return 0;
        }
    }
}