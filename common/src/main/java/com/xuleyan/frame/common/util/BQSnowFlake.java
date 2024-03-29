/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 *
 * @author xuleyan
 * 雪花算法
 * @version BQSnowFlake.java, v 0.1 2021-07-17 3:37 下午
 */
@Slf4j
public class BQSnowFlake {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 12;

    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT = 5;

    /**
     * 数据中心占用的位数
     */
    private final static long DATACENTER_BIT = 5;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;
    private final static String pattern = "yyyyMMddHHmmss";

    /**
     * 数据中心
     */
    private long datacenterId;

    /**
     * 机器标识
     */
    private long machineId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上一次时间戳
     */
    private long lastStmp = -1L;

    BQSnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    synchronized String nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;
        String time = DateFormatUtils.format(new Date(), pattern);

        System.out.println("time = " + time);
        long timestamp = (currStmp - START_STMP) << TIMESTMP_LEFT;
        System.out.println("timestamp ="+ timestamp);

        long dataCenterId = datacenterId << DATACENTER_LEFT;
        System.out.println("dataCenterId = "+ dataCenterId);

        long machineCenterId = machineId << MACHINE_LEFT;
        System.out.println("machineCenterId = "+ machineCenterId);

        System.out.println("sequence = "+ sequence);

        return time + (
                timestamp //时间戳部分
                | dataCenterId       //数据中心部分
                | machineCenterId             //机器标识部分
                | sequence);                  //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }


}