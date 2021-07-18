package com.xuleyan.frame.extend.redis.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jedis template
 */
public interface JedisTemplate {

    void destroy();

    /**
     * 删除键
     *
     * @param keys
     * @return
     */
    long del(String... keys);

    /**
     * 是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 过期时间
     *
     * @param key     键
     * @param seconds 秒数
     * @return
     */
    long expire(String key, int seconds);

    /**
     * 查找所有符合给定模式 pattern 的 key
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 字符串值 value 关联到 key
     * 覆盖型
     *
     * @param key
     * @param val
     * @return
     */
    String set(String key, String val);

    /**
     *  位图操作 在指定偏移上设置值
     * @param key
     * @param offset
     * @param val
     */
    boolean setbit(String key, long offset, boolean val);

    boolean setbit(String key, long offset, String val);

    /**
     * 计算范围内有值的位数
     * @param key
     * @param start
     * @param end
     * @return
     */
    Long bitcount(String key, long start, long end);

    Long bitcount(String key);

    /**
     * bitmap的位与 返回destKey的长度
     * @param destKey
     * @param srcKey
     * @return
     */
    Long bitand(String destKey, String... srcKey);

    /**
     * bitmap的或
     * @param destKey
     * @param srcKey
     * @return
     */
    Long bitor(String destKey, String... srcKey);

    /**
     * bitmap的异或
     * @param destKey
     * @param srcKey
     * @return
     */
    Long bitxor(String destKey, String... srcKey);

    /**
     * bitmap的非
     * @param destKey
     * @param srcKey
     * @return
     */
    Long bitnot(String destKey, String srcKey);

    /**
     * 只有在 key 不存在时设置 key 的值 可用于分布式锁
     *
     * @param key     键
     * @param value   值
     * @param timeout 超时时间,单位秒
     * @return 之前已经存在返回false, 不存在返回true
     */
    boolean setIfAbsent(String key, String value, long timeout);

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 若给定的 key 已经存在，则 SETNX 不做任何动作。
     *
     * @param key
     * @param val
     * @return
     */
    long setNx(String key, String val);

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     *
     * @param key
     * @param val
     * @param seconds
     * @return
     */
    String setEx(String key, String val, int seconds);

    /**
     * 返回 key 所关联的字符串值。
     * 如果 key 不存在那么返回特殊值 nil 。
     * 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 批量返回key所关联的字符串值
     *
     * @param keys
     * @return
     */
    List<String> mget(String... keys);


    /**
     * 将 key 所储存的值加上增量 increment 。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     *
     * @param key
     * @param increment
     * @return
     */
    long increBy(String key, long increment);

    long decrBy(String key, long increment);


    long hdel(String hashkey, String... keys);

    boolean hexists(String hashkey, String key);

    String hget(String hashkey, String key);

    Map<String, String> hgetAll(String hashkey);

    Long hincrby(String hashkey, String key, long increment);

    Set<String> hkeys(String hashkey);

    long hlen(String hashkey);

    long hsetnx(String hashkey, String key, String val);

    long hset(String hashkey, String key, String val);

    long hdel(String hashkey, String key);

    String hmset(String hashkey, Map<String, String> mapdata);

    long sadd(String hashkey, String... members);

    long scard(String hashkey);

    Set<String> smembers(String hashkey);

    long srem(String hashkey, String... members);

    String spop(String hashkey);

    long zadd(String hashkey, String member, double score);

    long zadd(String hashkey, Map<String, Double> scoreMembers);

    long zcard(String hashkey);

    long zcount(String hashkey, double minScore, double maxScore);

    double zincrby(String hashkey, String key, double increment);

    Set<String> zrange(String hashkey, long start, long stop);

    Set<String> zrangeByScore(String hashkey, double minScore, double maxScore);

    long zrank(String hashkey, String member);

    Long zRemove(String key, String... values);

    long llen(String lkey);

    String lindex(String lkey, long index);

    long lpush(String lkey, String... members);

    String lpop(String lkey);

    long rpush(String lkey, String... members);

    String rpop(String lkey);

    List<String> blpop(String lkey, int timeout);

    List<String> brpop(String lkey, int timeout);

    String lset(String lkey, long index, String value);

    List<String> lrange(String lkey, long start, long stop);

    long lrem(String lkey, long count, String value);

    Object eval(String script, List<String> keys, List<String> args);
}
