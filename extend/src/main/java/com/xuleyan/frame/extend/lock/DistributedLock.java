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
     * zookeeper 尝试获取锁，首先获取可重入锁对象，然后尝试获取锁，
     * 若当前线程已获取该锁，则将该锁的lockCount + 1并返回获取锁成功
     * 若不是当前线程，则首先在/mylock目录下创建一个临时顺序节点，然后获取该目录下的所有临时节点，
     *  判断当前节点是否获取到锁（即是否是最小的节点），若是则返回获取锁成功，若不是则创建一个watcher监听它的上一个节点，
     *  若直到有效时间截止还没获取到锁则失败，否则成功
     * @param lockKey 锁的key
     */
    boolean tryLock(String lockKey, long timeout) throws Exception;

    /**
     * 解锁
     * @param lockKey 锁的key
     */
    void unlock(String lockKey) throws Exception;
}
