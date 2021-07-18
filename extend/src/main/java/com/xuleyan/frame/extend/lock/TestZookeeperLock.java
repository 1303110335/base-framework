package com.xuleyan.frame.extend.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 测试zookeeper分布式锁
 */
@Slf4j
public class TestZookeeperLock {

    public static void main(String[] args) throws Exception {
        //创建zookeeper的客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(100, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", retryPolicy);
        client.start();

        DistributedLock distributedLock = new ZookeeperDistributedLock(client, "/curator");
//        distributedLock.lock("/lock");
        String lockKey = "lock";
        Callable<Boolean> callable = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return distributedLock.tryLock(lockKey);
            }
        };

        FutureTask<Boolean> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        Boolean aBoolean = futureTask.get();
        System.out.println(aBoolean);

        distributedLock.tryLock(lockKey);
        System.out.printf("mutex");
        distributedLock.unlock(lockKey);
        client.close();
    }
}
