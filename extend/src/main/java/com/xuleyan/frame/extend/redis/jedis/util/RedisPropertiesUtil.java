package com.xuleyan.frame.extend.redis.jedis.util;

import com.xuleyan.frame.common.exception.BQException;
import com.xuleyan.frame.common.util.PropertyUtils;
import com.xuleyan.frame.extend.redis.jedis.config.RedisProperties;
import com.xuleyan.frame.extend.redis.jedis.enums.RedisServeEnum;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2019-11-18
 **/
public class RedisPropertiesUtil {


    public static RedisProperties parseConf(String filePath) throws IOException {
        Properties properties = PropertyUtils.parseProperties(filePath);
        String type = properties.getProperty("xuleyan.cache.redis.type");
        String master = properties.getProperty("xuleyan.cache.redis.master");
        String password = properties.getProperty("xuleyan.cache.redis.password");
        String address = properties.getProperty("xuleyan.cache.redis.address");

        int dbIndex = getInt(properties,"xuleyan.cache.redis.dbIndex", 0);
        int maxTotal = getInt(properties,"xuleyan.cache.redis.maxTotal", 8);
        int maxIdle = getInt(properties,"xuleyan.cache.redis.maxIdle", 8);
        int minIdle = getInt(properties,"xuleyan.cache.redis.minIdle", 0);

        RedisProperties redisConf = new RedisProperties();
        redisConf.setMaster(master);
        redisConf.setAddress(address);
        redisConf.setType(RedisServeEnum.parseByType(type));
        redisConf.setPassword(password);
        redisConf.setDbIndex(dbIndex);

        redisConf.setMaxTotal(maxTotal);
        redisConf.setMaxIdle(maxIdle);
        redisConf.setMinIdle(minIdle);
        if(StringUtils.isEmpty(address)){
            throw new BQException("address is empty");
        }

        return redisConf;
    }

    private static int getInt(Properties propertie, String key, int defaultVal) {
        String proVal = propertie.getProperty(key);
        if(proVal != null){
            return Integer.parseInt(proVal);
        }
        return defaultVal;
    }
}
