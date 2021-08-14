/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.utils;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author xuleyan
 * @version TraceIdGeneratorUtils.java, v 0.1 2021-07-23 3:23 下午
 */
public class TraceIdGeneratorUtils {

    private static String IP_16 = "ffffffff";

    private static final AtomicInteger count = new AtomicInteger(1000);


    public static String getTraceId(HttpServletRequest request) {
        String netIp = IpUtils.getNetIpAddr(request);
        return getTraceId(netIp);
    }

    public static String getTraceId(String netIp) {
        return getTraceId(getIP_16(null == netIp ? IP_16 : netIp), Clock.systemUTC().millis(), getNextId());
    }

    private static String getTraceId(String ip, long timestamp, int nextId) {
        StringBuilder appender = new StringBuilder(32);
        StringBuilder result = appender.append(RuntimeUtils.getPid()).append(ip).append(timestamp).append(nextId);
        return result.toString();
    }

    private static int getNextId() {
        for (; ;) {
            int current = count.get();
            int next = (current > 9000) ? 1000 : current + 1;
            if (count.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    /**
     * 将ip转化为16进制
     * @param ip
     * @return
     */
    private static String getIP_16(String ip) {
        String[] ips = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String column : ips) {
            String hex = Integer.toHexString(Integer.parseInt(column));
            if (hex.length() == 1) {
                sb.append('0').append(hex);
            } else {
                sb.append(hex);
            }

        }
        return sb.toString();
    }

    public static String generate() {
        return getTraceId(IP_16, Clock.systemUTC().millis(), getNextId());
    }
}