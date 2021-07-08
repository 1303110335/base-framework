package com.xuleyan.frame.rpc.client;


import com.xuleyan.frame.rpc.RpcResult;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author xuleyan
 * @version Tuple.java, v 0.1 2021-07-08 9:17 下午
 */
public abstract class AbstClient implements Client {

    /**
     * 单位秒
     */
    public static final int TIME_OUT = 5 * 1000;
    /**
     * 请求序列串
     */
    private static AtomicLong requestUuid = new AtomicLong(1);
    /**
     * 请求结果存储
     */
    private static ConcurrentHashMap<Long, ArrayBlockingQueue<RpcResult>> responseUuidMap = new ConcurrentHashMap<>();

    protected String host;
    protected int port;
    private String token;

    /**
     * @param host 服务器地址
     * @param port 端口
     * @param token 请求加密token
     */
    public AbstClient(String host, int port, String token) {
        this.host = host;
        this.port = port;
        this.token = token;
    }

    /**
     * 设置相应结果
     * @param requestNo
     * @param rpcResult
     */
    public static void setResponse(Long requestNo, RpcResult rpcResult) {
        ArrayBlockingQueue<RpcResult> responseQueue = responseUuidMap.get(requestNo);
        if(responseQueue == null){
            responseQueue = new ArrayBlockingQueue<RpcResult>(1);
        }
        if(rpcResult != null) {
            responseQueue.add(rpcResult);
        }
        responseUuidMap.putIfAbsent(requestNo, responseQueue);
    }

    /**
     * 获取相应结果
     * @param requestNo
     * @return
     */
    @Override
    public RpcResult getResponse(Long requestNo) {
        ArrayBlockingQueue<RpcResult> responseQueue = responseUuidMap.get(requestNo);
        if (responseQueue != null) {
            try {
                RpcResult response = responseQueue.poll(TIME_OUT, TimeUnit.MILLISECONDS);
                return response;
            } catch (InterruptedException e) {
                return RpcResult.instance(e);
            }finally{
                responseUuidMap.remove(requestNo);
                close();
            }
        }
        return RpcResult.instance(new NullPointerException("requestNo: " + requestNo + " is not exists!"));
    }

    public Long getUuid(){
        return requestUuid.getAndIncrement();
    }

    public String getToken() {
        return token;
    }

    public abstract void close();
}
