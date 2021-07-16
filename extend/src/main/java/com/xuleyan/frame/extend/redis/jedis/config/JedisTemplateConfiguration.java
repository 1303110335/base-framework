/**
 * xuleyan.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.extend.redis.jedis.config;

import com.xuleyan.frame.extend.redis.jedis.JedisTemplate;
import com.xuleyan.frame.extend.redis.jedis.impl.JedisClusterTemplate;
import com.xuleyan.frame.extend.redis.jedis.impl.JedisSentinelTemplate;
import com.xuleyan.frame.extend.redis.jedis.impl.JedisStandaloneTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.Resource;

/**
 *
 * @author xuleyan
 * @version JedisTemplateConfiguration.java, v 0.1 2020-08-02 2:44 下午
 */
@Configuration
public class JedisTemplateConfiguration {

    @Resource(name = "jedisPool")
    private Object jedisPool;

    @DependsOn("jedisPool")
    @Bean(value = "jedisTemplate", destroyMethod = "destroy")
    public JedisTemplate jedisTemplate() {
        if (JedisPoolConfiguration.redisProperties == null) {
            throw new NullPointerException("JedisTemplateConfiguration.redisConf is null!");
        }

        JedisTemplate jedisTemplate = null;
        switch (JedisPoolConfiguration.redisProperties.getType()) {
            case cluster:
                jedisTemplate = new JedisClusterTemplate();
                ((JedisClusterTemplate)jedisTemplate).setJedisCluster((JedisCluster) jedisPool);
                break;
            case standalone:
                jedisTemplate = new JedisStandaloneTemplate();
                ((JedisStandaloneTemplate) jedisTemplate).setJedisPool((JedisPool) jedisPool);
                break;

            case sentinel:
                jedisTemplate = new JedisSentinelTemplate();
                ((JedisSentinelTemplate) jedisTemplate).setJedisPool((JedisSentinelPool) jedisPool);
                break;
            default:
                throw new IllegalArgumentException("redis 类型配置错误");
        }
        return jedisTemplate;
    }
}