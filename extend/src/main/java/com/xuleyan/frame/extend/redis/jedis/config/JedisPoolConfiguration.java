/**
 * xuleyan.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.extend.redis.jedis.config;

import com.alibaba.fastjson.JSON;
import com.xuleyan.frame.common.exception.BQException;
import com.xuleyan.frame.extend.redis.jedis.util.RedisPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author xuleyan
 * @version JedisPoolConfiguration.java, v 0.1 2020-08-02 11:42 上午
 */
@Configuration
public class JedisPoolConfiguration {

    @Value("${xuleyan.redis.conf}")
    private String redisConf;

    static RedisProperties redisProperties;

    @Bean(value = {"jedisPool"})
    public Object jedisPool() throws IOException {
        if (StringUtils.isEmpty(redisConf)) {
            throw new IllegalArgumentException("缺少配置: xuleyanredis.conf=redis-xxx.properties");
        }
        redisProperties = RedisPropertiesUtil.parseConf(redisConf);
        if (redisProperties.getNodes() == null) {
            throw new BQException("redis node is null");
        }
        String password = redisProperties.getPassword();
        switch (redisProperties.getType()) {
            case cluster:
                if (redisProperties.getNodes().size() < 1) {
                    throw new BQException("Redis cluster nodes is 1");
                }
                if (password != null && password.length() > 0) {
                    return new JedisCluster(redisProperties.getNodes(), 20000, 2000, 5, password, redisProperties.getPoolConfig());
                }
                return new JedisCluster(redisProperties.getNodes(), redisProperties.getPoolConfig());
            case standalone:
                HostAndPort hostAndPort = redisProperties.getNode();
                if (password != null && !"".equals(password.trim())) {
                    return new JedisPool(redisProperties.getPoolConfig(), hostAndPort.getHost(), hostAndPort.getPort(), 20000, password, redisProperties.getDbIndex());
                }
                return new JedisPool(redisProperties.getPoolConfig(), hostAndPort.getHost(), hostAndPort.getPort(), redisProperties.getDbIndex());

            case sentinel:
                Set<String> sentinels = new HashSet<>();
                for(HostAndPort node : redisProperties.getNodes()){
                    sentinels.add(node.toString());
                }
                return new JedisSentinelPool("yewu01", sentinels, redisProperties.getPoolConfig());
            default:
                throw new BQException("Redis配置错误: " + JSON.toJSONString(redisProperties));
        }
    }

}