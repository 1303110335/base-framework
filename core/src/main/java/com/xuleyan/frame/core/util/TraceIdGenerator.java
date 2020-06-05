/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;


import com.xuleyan.frame.core.constants.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuleyan
 * @version TraceIdGenerator.java, v 0.1 2020-05-29 9:27 PM xuleyan
 */
@Slf4j
public class TraceIdGenerator {

    private static String IP_16 = "ffffffff";

    private static AtomicInteger count = new AtomicInteger(1000);

    private static String P_ID_CACHE = null;

    static {
        String ipAddress = getInetAddress();
        if (ipAddress != null) {
            IP_16 = getIp16(ipAddress);
        }
    }

    private static String getIp16(String ipAddress) {
        String[] ips = ipAddress.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String ip : ips) {
            String s = Integer.toHexString(Integer.parseInt(ip));
            if (s.length() == 1) {
                sb.append('0').append(s);
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private static String getInetAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && !address.getHostAddress().contains(":")) {
                        return address.getHostAddress();
                    }

                }
            }
        } catch (SocketException e) {
            LogUtil.error(log, "获取IP地址SocketException异常,e={}", e);
        }
        return null;
    }

    public static String generate() {
        return getTraceId(IP_16, System.currentTimeMillis(), getNextId());

    }

    private static String getTraceId(String ip, long timestamp, int nextId) {
        return ip + timestamp + nextId + getPID();
    }

    private static String getPID() {
        if (P_ID_CACHE != null) {
            return P_ID_CACHE;
        }

        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isBlank(processName)) {
            return StringPool.EMPTY;
        }

        String[] processSplitNames = processName.split(StringPool.AT);
        if (processSplitNames.length == 0) {
            return StringPool.EMPTY;
        }
        String pid = processSplitNames[0];

        if (StringUtils.isBlank(pid)) {
            return StringPool.EMPTY;
        }
        P_ID_CACHE = pid;
        return pid;
    }

    private static int getNextId() {
        for (; ; ) {
            int current = count.get();
            int next = current > 9000 ? 1000 : current + 1;
            if (count.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    public static void main(String[] args) {
        String inetAddress = TraceIdGenerator.getInetAddress();

        System.out.println(getIp16(inetAddress));

        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(processName);

        System.out.println(TraceIdGenerator.generate());
    }

}