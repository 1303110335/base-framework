/**
 * bianque.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.extend.redis.jedis.config;

import com.xuleyan.frame.common.exception.BQException;
import com.xuleyan.frame.extend.redis.jedis.enums.RedisServeEnum;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author xuleyan
 * @version RedisProperties.java, v 0.1 2020-08-02 11:45 上午
 */
public class RedisProperties {
    private RedisServeEnum serveType = RedisServeEnum.cluster;

    /**
     * 节点master
     */
    private String master;

    /**
     * 格式
     * 127.0.0.1:6379,127.0.0.1:6378,...
     */
    private String address;

    /**
     * 数据库序号，默认0
     */
    private int dbIndex = 0;
    /**
     * 密码
     */
    private String password;

    private int maxTotal = 8;
    private int maxIdle = 8;
    private int minIdle = 4;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RedisServeEnum getType() {
        return serveType;
    }

    public void setType(RedisServeEnum type) {
        this.serveType = type;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }

    @Override
    public String toString() {
        return "RedisProperites{" +
                "type='" + serveType + '\'' +
                ", master='" + master + '\'' +
                ", address='" + address + '\'' +
                ", dbIndex=" + dbIndex +
                ", password='" + password + '\'' +
                ", maxTotal=" + maxTotal +
                ", maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                '}';
    }

    public GenericObjectPoolConfig getPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        if (config.getMaxTotal() > 0) {
            config.setMaxTotal(getMaxTotal());
        }

        if (config.getMaxIdle() > 0) {
            config.setMaxIdle(getMaxIdle());
        }

        if (config.getMinIdle() > 0) {
            config.setMinIdle(getMinIdle());
        }

        return config;
    }

    public Set<HostAndPort> getNodes() throws BQException {
        String[] hostAndPorts = getAddrIp();
        if (hostAndPorts.length < 1) {
            throw new BQException("redis node more than 1");
        }
        Set<HostAndPort> items = new HashSet<>();
        for (String ipPortItem : hostAndPorts) {
            String[] ipPort = ipPortItem.split(":");
            items.add(new HostAndPort(ipPort[0], Integer.parseInt(ipPort[1])));
        }
        return items;
    }

    public HostAndPort getNode() throws BQException {
        String[] hostAndPorts = getAddrIp();
        if (hostAndPorts.length != 1) {
            throw new BQException("redis node more than 1");
        }
        String[] hostAndPortStrs = hostAndPorts[0].split(":");
        return new HostAndPort(hostAndPortStrs[0], Integer.parseInt(hostAndPortStrs[1]));
    }

    public String[] getAddrIp() {
        if (getAddress() == null || "".equals(getAddress())) {
            return new String[0];
        }
        return getAddress().split(",");
    }
}