package com.xuleyan.frame.extend.lock;

import com.xuleyan.frame.common.exception.CommonException;
import com.xuleyan.frame.core.constants.StringPool;
import com.xuleyan.frame.core.util.LogUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 基于zookeeper的分布式锁
 */
@Slf4j
public class ZookeeperDistributedLock implements DistributedLock {

    /**
     * 锁缓存
     */
    private Map<String, InterProcessMutex> lockMap = new ConcurrentHashMap<>(16);

    /**
     * zookeeper客户端
     */
    private CuratorFramework curatorFramework;

    /**
     * 总计父锁目录
     */
    private String lockBasePath;

    public ZookeeperDistributedLock(CuratorFramework curatorFramework, String lockBasePath) {
        this.curatorFramework = curatorFramework;
        this.lockBasePath = createLockBasePath(lockBasePath);
    }

    private String createLockBasePath(String lockBasePath) {
        if (lockBasePath == null) {
            return StringPool.EMPTY;
        }

        if (!lockBasePath.endsWith(StringPool.SLASH)) {
            lockBasePath = lockBasePath + StringPool.SLASH;
        }
        return lockBasePath;
    }

    @Override
    public void lock(String lockKey) throws Exception {
        String lockPath = getLockPath(lockKey);
        long currentTime = System.currentTimeMillis();
        try {
            InterProcessMutex lock = getInterProcessMutex(lockPath);
            lock.acquire();
        } catch (Exception e) {
            LogUtil.error(log, "【分布式锁服务】lock-获取分布式锁失败!lockPath = {}", lockPath, e);
            throw new Exception("【分布式锁服务】lock-获取分布式锁失败!");
        }

        LogUtil.info(log, "【分布式锁服务】lock-获取分布式锁成功!lockPath = {}, 耗时 = {}ms",
                lockPath, System.currentTimeMillis() - currentTime);
    }

    /**
     * 获取curator可重入锁对象
     * @param lockKey
     * @return
     */
    private InterProcessMutex getInterProcessMutex(String lockKey) {
        InterProcessMutex interProcessMutex = lockMap.get(lockKey);
        if (interProcessMutex == null) {
            synchronized (lockMap) {
                interProcessMutex = new InterProcessMutex(curatorFramework, lockKey);
                lockMap.put(lockKey, interProcessMutex);
            }
        }
        return interProcessMutex;
    }

    private String getLockPath(String lockKey) {
        if (StringUtils.isBlank(lockKey)) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance("【zk分布式锁】lockKey不能为空");
        }

        return lockBasePath + lockKey;
    }

    @Override
    public boolean tryLock(String lockKey) throws Exception {
        return tryLock(lockKey, 0);
    }

    @Override
    public boolean tryLock(String lockKey, long timeout) throws Exception {
        String lockPath = getLockPath(lockKey);
        long currentTime = System.currentTimeMillis();
        try {
            InterProcessMutex lock = getInterProcessMutex(lockPath);
            lock.acquire(timeout, TimeUnit.MILLISECONDS);
            LogUtil.info(log, "【分布式锁服务】tryLock-获取到分布式锁成功! 耗时={}ms, lockPath={}", System.currentTimeMillis() - currentTime, lockPath);
        } catch (Exception e) {
            LogUtil.error(log, "【分布式锁服务】lock-获取分布式锁失败!lockPath = {}", lockPath, e);
            throw new Exception("【分布式锁服务】lock-获取分布式锁失败! lockPath = " + lockPath);
        }
        return true;
    }

    @Override
    public void unlock(String lockKey) throws Exception {
        String lockPath = getLockPath(lockKey);
        try {
            InterProcessMutex lock = getInterProcessMutex(lockPath);
            lock.release();
        } catch (Exception e) {
            LogUtil.error(log, "【分布式服务】unlock-分布式锁解锁失败！lockPath = {}", lockPath, e);
            throw new Exception("【分布式服务】unlock-分布式锁解锁失败");
        }
    }
}
