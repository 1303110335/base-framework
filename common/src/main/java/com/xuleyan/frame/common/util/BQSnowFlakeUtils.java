/**
 * bianque.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.common.util;

/**
 *
 * @author xuleyan
 * @version BQSnowFlakeUtils.java, v 0.1 2021-07-17 3:39 下午
 */
public class BQSnowFlakeUtils {

    private BQSnowFlakeUtils() {
    }

    private static BQSnowFlake bqSnowFlake;

    /**
     * 32位 20191011115117380001473842655232
     */
    public static String generate() {
        if (null == bqSnowFlake) {
            synchronized (BQSnowFlakeUtils.class) {
                if (null == bqSnowFlake) {
                    bqSnowFlake = genBQSnowFlake();
                }
            }
        }
        return bqSnowFlake.nextId();
    }

    public static BQSnowFlake getBqSnowFlake() {
        return bqSnowFlake;
    }

    public static void setBqSnowFlake(BQSnowFlake bqSnowFlake) {
        BQSnowFlakeUtils.bqSnowFlake = bqSnowFlake;
    }

    private static BQSnowFlake genBQSnowFlake() {
        String replace = IPUtils.getLocalIpAddr();
        if (null == replace) {
            replace = "127.0.0.1";
        }

        int hashCode = replace.hashCode() < 0 ? ~replace.hashCode() + 1 : replace.hashCode();
        hashCode = hashCode % 31;

        return new BQSnowFlake(hashCode, hashCode + 1);
    }

    public static void main(String[] args) {
        String generate = BQSnowFlakeUtils.generate();
        System.out.println(generate);
    }
}