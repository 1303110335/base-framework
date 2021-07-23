/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author xuleyan
 * @version IpUtils.java, v 0.1 2021-07-23 3:27 下午
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_HOST_V4 = "127.0.0.1";
    private static final String LOCAL_HOST_V6 = "0:0:0:0:0:0:0:1";
    private static final int IP_V4_LENGTH = 15;
    private static final String SEPARATOR = ",";

    public static String getNetIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCAL_HOST_V4.equals(ipAddress) || LOCAL_HOST_V6.equals(ipAddress)) {
                /* 根据网卡取本机配置的IP */
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    return null;
                }
                ipAddress = inet.getHostAddress();
            }
        }
        /* 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割 */
        if (ipAddress != null && ipAddress.length() > IP_V4_LENGTH) {
            if (ipAddress.indexOf(SEPARATOR) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(SEPARATOR));
            }
        }

        return ipAddress;
    }
}