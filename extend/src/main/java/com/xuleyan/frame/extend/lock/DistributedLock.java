package com.xuleyan.frame.extend.lock;

/**
 * 分布式锁服务
 */
public interface DistributedLock {
    /**
     * 获取锁，如遇到锁竞争则阻塞知道获取到锁
     * @param lockKey 锁的key
     * @throws Exception
     */
    void lock(String lockKey) throws Exception;

    /**
     * 尝试获取锁，并立即返回结果
     * @param lockKey 锁的key
     */
    boolean tryLock(String lockKey) throws Exception;

    /**
     * 在指定时间内尝试获取锁
     * @param lockKey 锁的key
     */
    boolean tryLock(String lockKey, long timeout) throws Exception;

    /**
     * 解锁
     * @param lockKey 锁的key
     */
    void unlock(String lockKey) throws Exception;
}
