package com.xuleyan.frame.extend.redis.jedis.enums;

/**
 * @author: kui.zhouk
 * @date: 2019-03-21
 */
public enum RedisServeEnum {

    /**
     * 集群模式, 多master多slave
     */
    cluster,
    /**
     * 单机模式
     */
    standalone,
    /**
     * 哨兵模式, 单master不少于一个slave
     */
    sentinel;

    public static RedisServeEnum parseByType(String type) {
        for(RedisServeEnum redisServeEnum : RedisServeEnum.values()) {
            if(redisServeEnum.name().equals(type)) {
                return redisServeEnum;
            }
        }
        return null;
    }

}
