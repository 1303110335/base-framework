package com.xuleyan.frame.extend.redis.jedis.impl;

import com.xuleyan.frame.extend.redis.jedis.JedisTemplate;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisSentinelTemplate implements JedisTemplate {

    private JedisSentinelPool jedisPool;

    @Override
    public void destroy() {
        jedisPool.destroy();
    }

    @Override
    public long del(String... keys) {
        Jedis jedis = jedisPool.getResource();
        try {
            for (String key : keys) {
                jedis.del(key);
            }
            return keys.length;
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.expire(key, seconds);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public String set(String key, String val) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.set(key, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean setbit(String key, long offset, boolean val) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setbit(key, offset, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean setbit(String key, long offset, String val) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setbit(key, offset, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.bitcount(key, start, end);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long bitcount(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.bitcount(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long bitand(String destKey, String... srcKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.bitop(BitOP.AND, destKey, srcKey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long bitor(String destKey, String... srcKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.bitop(BitOP.OR, destKey, srcKey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long bitxor(String destKey, String... srcKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.bitop(BitOP.XOR, destKey, srcKey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long bitnot(String destKey, String srcKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.bitop(BitOP.NOT, destKey, srcKey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean setIfAbsent(String key, String value, long timeout) {
        Jedis jedis = jedisPool.getResource();
        try {
            SetParams setParam = new SetParams();
            setParam.ex((int)timeout);
            setParam.nx();
            String result = jedis.set(key, value, setParam);
            return "OK".equals(result);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long setNx(String key, String val) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setnx(key, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String setEx(String key, String val, int seconds) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setex(key, seconds, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public List<String> mget(String... keys) {
        List<String> datas = new ArrayList<>(keys.length);
        Jedis jedis = jedisPool.getResource();
        try {
            for (String key : keys){
                datas.add(jedis.get(key));
            }
            return datas;
        } finally {
            jedis.close();
        }
    }

    @Override
    public long increBy(String key, long increment) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incrBy(key, increment);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long decrBy(String key, long increment) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.decrBy(key, increment);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long hdel(String hashkey, String... keys) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hdel(hashkey, keys);
        } finally {
            jedis.close();
        }
    }

    @Override
    public boolean hexists(String hashkey, String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hexists(hashkey, key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String hget(String hashkey, String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hget(hashkey, key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Map<String, String> hgetAll(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hgetAll(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long hincrby(String hashkey, String key, long increment) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hincrBy(hashkey, key, increment);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Set<String> hkeys(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hkeys(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long hlen(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hlen(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long hsetnx(String hashkey, String key, String val) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hsetnx(hashkey, key, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long hset(String hashkey, String key, String val) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hset(hashkey, key, val);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long hdel(String hashkey, String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hdel(hashkey, key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String hmset(String hashkey, Map<String, String> mapdata) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hmset(hashkey, mapdata);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long sadd(String hashkey, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.sadd(hashkey, members);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long scard(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.scard(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Set<String> smembers(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.smembers(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long srem(String hashkey, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.srem(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String spop(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.spop(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long zadd(String hashkey, String member, double score) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zadd(hashkey, score, member);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long zadd(String hashkey, Map<String, Double> scoreMembers) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zadd(hashkey, scoreMembers);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long zcard(String hashkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zcard(hashkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long zcount(String hashkey, double minScore, double maxScore) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zcount(hashkey, minScore, maxScore);
        } finally {
            jedis.close();
        }
    }

    @Override
    public double zincrby(String hashkey, String key, double increment) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zincrby(hashkey, increment, key);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Set<String> zrange(String hashkey, long start, long stop) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrange(hashkey, start, stop);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Set<String> zrangeByScore(String hashkey, double minScore, double maxScore) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrangeByScore(hashkey, minScore, maxScore);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long zrank(String hashkey, String member) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrank(hashkey, member);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Long zRemove(String key, String... values) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrem(key, values);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long llen(String lkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.llen(lkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String lindex(String lkey, long index) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lindex(lkey, index);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long lpush(String lkey, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lpush(lkey, members);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String lpop(String lkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lpop(lkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long rpush(String lkey, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.rpush(lkey, members);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String rpop(String lkey) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.rpop(lkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public List<String> blpop(String lkey, int timeout) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.blpop(timeout, lkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public List<String> brpop(String lkey, int timeout) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.brpop(timeout, lkey);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String lset(String lkey, long index, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lset(lkey, index, value);
        } finally {
            jedis.close();
        }
    }

    @Override
    public List<String> lrange(String lkey, long start, long stop) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lrange(lkey, start, stop);
        } finally {
            jedis.close();
        }
    }

    @Override
    public long lrem(String lkey, long count, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lrem(lkey, count, value);
        } finally {
            jedis.close();
        }
    }

    public void setJedisPool(JedisSentinelPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Object eval(String script, List<String> keys, List<String> params) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.eval(script, keys, params);
        } finally {
            jedis.close();
        }
    }
}