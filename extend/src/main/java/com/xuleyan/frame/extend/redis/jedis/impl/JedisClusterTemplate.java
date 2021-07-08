package com.xuleyan.frame.extend.redis.jedis.impl;

import com.xuleyan.frame.extend.redis.jedis.JedisTemplate;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisClusterTemplate implements JedisTemplate {

    private JedisCluster jedisCluster;


    @Override
    public void destroy() {
//        try {
//            jedisCluster.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public long del(String... keys) {
        return jedisCluster.del(keys);
    }

    @Override
    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public long expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    @Override
    public Set<String> keys(String pattern) {

        Set<String> keys = new HashSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                keys.addAll(connection.keys(pattern));
            } finally {
                connection.close();//用完一定要close这个链接！！！
            }
        }
        return keys;
    }

    @Override
    public String set(String key, String val) {
        return jedisCluster.set(key, val);
    }

    @Override
    public boolean setbit(String key, long offset, boolean val) {
        return jedisCluster.setbit(key, offset, val);
    }

    @Override
    public boolean setbit(String key, long offset, String val) {
        return jedisCluster.setbit(key, offset, val);
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        return jedisCluster.bitcount(key, start, end);
    }

    @Override
    public Long bitcount(String key) {
        return jedisCluster.bitcount(key);
    }

    @Override
    public Long bitand(String destKey, String... srcKey) {
        return jedisCluster.bitop(BitOP.AND, destKey, srcKey);
    }

    @Override
    public Long bitor(String destKey, String... srcKey) {
        return jedisCluster.bitop(BitOP.OR, destKey, srcKey);
    }

    @Override
    public Long bitxor(String destKey, String... srcKey) {
        return jedisCluster.bitop(BitOP.XOR, destKey, srcKey);
    }

    @Override
    public Long bitnot(String destKey, String srcKey) {
        return jedisCluster.bitop(BitOP.NOT, destKey, srcKey);
    }

    @Override
    public boolean setIfAbsent(String key, String value, long timeout) {
        SetParams setParam = new SetParams();
        setParam.ex((int)timeout);
        setParam.nx();
        String res = jedisCluster.set(key, value, setParam);
        return "OK".equals(res);
    }

    @Override
    public long setNx(String key, String val) {
        return jedisCluster.setnx(key, val);
    }

    @Override
    public String setEx(String key, String val, int seconds) {
        return jedisCluster.setex(key, seconds, val);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public List<String> mget(String... keys) {
        return jedisCluster.mget(keys);
    }

    @Override
    public long increBy(String key, long increment) {
        return jedisCluster.incrBy(key, increment);
    }

    @Override
    public long decrBy(String key, long increment) {
        return jedisCluster.incrBy(key, increment);
    }

    @Override
    public long hdel(String hashkey, String... keys) {
        return jedisCluster.hdel(hashkey, keys);
    }

    @Override
    public boolean hexists(String hashkey, String key) {
        return jedisCluster.hexists(hashkey, key);
    }

    @Override
    public String hget(String hashkey, String key) {
        return jedisCluster.hget(hashkey, key);
    }

    @Override
    public Map<String, String> hgetAll(String hashkey) {
        return jedisCluster.hgetAll(hashkey);
    }

    @Override
    public Long hincrby(String hashkey, String key, long increment) {
        return jedisCluster.hincrBy(hashkey, key, increment);
    }

    @Override
    public Set<String> hkeys(String hashkey) {
        return jedisCluster.hkeys(hashkey);
    }

    @Override
    public long hlen(String hashkey) {
        return jedisCluster.hlen(hashkey);
    }

    @Override
    public long hsetnx(String hashkey, String key, String val) {
        return jedisCluster.hsetnx(hashkey, key, val);
    }

    @Override
    public long hset(String hashkey, String key, String val) {
        return jedisCluster.hset(hashkey, key, val);
    }

    @Override
    public long hdel(String hashkey, String key) {
        return jedisCluster.hdel(hashkey, key);
    }

    @Override
    public String hmset(String hashkey, Map<String, String> mapdata) {
        return jedisCluster.hmset(hashkey, mapdata);
    }

    @Override
    public long sadd(String skey, String... members) {
        return jedisCluster.sadd(skey, members);
    }

    @Override
    public long scard(String skey) {
        return jedisCluster.scard(skey);
    }

    @Override
    public Set<String> smembers(String skey) {
        return jedisCluster.smembers(skey);
    }

    @Override
    public long srem(String skey, String... members) {
        return jedisCluster.srem(skey, members);
    }

    @Override
    public String spop(String skey) {
        return jedisCluster.spop(skey);
    }

    @Override
    public long zadd(String zkey, String member, double score) {
        return jedisCluster.zadd(zkey, score, member);
    }

    @Override
    public long zadd(String zkey, Map<String, Double> scoreMembers) {
        return jedisCluster.zadd(zkey, scoreMembers);
    }

    @Override
    public long zcard(String zkey) {
        return jedisCluster.zcard(zkey);
    }

    @Override
    public long zcount(String zkey, double minScore, double maxScore) {
        return jedisCluster.zcount(zkey, minScore, maxScore);
    }

    @Override
    public double zincrby(String zkey, String key, double increment) {
        return jedisCluster.zincrby(zkey, increment, key);
    }

    @Override
    public Set<String> zrange(String zkey, long start, long stop) {
        return jedisCluster.zrange(zkey, start, stop);
    }

    @Override
    public Set<String> zrangeByScore(String zkey, double minScore, double maxScore) {
        return jedisCluster.zrangeByScore(zkey, minScore, maxScore);
    }

    @Override
    public long zrank(String zkey, String member) {
        return jedisCluster.zrank(zkey, member);
    }

    @Override
    public Long zRemove(String key, String... values) {
        return jedisCluster.zrem(key, values);
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public long llen(String lkey) {
        return jedisCluster.llen(lkey);
    }

    @Override
    public String lindex(String lkey, long index) {
        return jedisCluster.lindex(lkey, index);
    }

    @Override
    public long lpush(String lkey, String... members) {
        return jedisCluster.lpush(lkey, members);
    }

    @Override
    public String lpop(String lkey) {
        return jedisCluster.lpop(lkey);
    }

    @Override
    public long rpush(String lkey, String... members) {
        return jedisCluster.rpush(lkey, members);
    }

    @Override
    public String rpop(String lkey) {
        return jedisCluster.rpop(lkey);
    }

    @Override
    public List<String> blpop(String lkey, int timeout) {
        return jedisCluster.blpop(timeout, lkey);
    }

    @Override
    public List<String> brpop(String lkey, int timeout) {
        return jedisCluster.brpop(timeout, lkey);
    }

    @Override
    public String lset(String lkey, long index, String value) {
        return jedisCluster.lset(lkey, index, value);
    }

    @Override
    public List<String> lrange(String lkey, long start, long stop) {
        return jedisCluster.lrange(lkey, start, stop);
    }

    @Override
    public long lrem(String lkey, long count, String value) {
        return jedisCluster.lrem(lkey, count, value);
    }
}