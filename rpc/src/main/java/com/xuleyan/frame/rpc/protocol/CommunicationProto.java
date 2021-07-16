/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc.protocol;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author xuleyan
 * @version CommunicationProto.java, v 0.1 2021-07-08 9:39 下午
 */
public abstract class CommunicationProto implements Serializable {

    /* 默认请求协议一个字节 */
    private static final int PROTOCOL_LEN = 1;
    /**
     * 请求ID
     */
    private long uuid;
    /**
     * 通信key, 便于服务分发
     * 8位字符串: 强制数字或者字母
     */
    private String key;

    /**
     * 请求秘钥
     * 8位字符串: 强制数字或者字母
     */
    private String token;

    /**
     * 通信数据
     */
    private byte[] body;

    private Throwable e;

    /**
     * 获取请求提对应的类实体
     * @return
     */
    public abstract Object body2Obj() throws IOException;

    /**
     * 设置请求体
     * @param bodyObj
     * @throws IOException
     */
    public abstract void setBody(Object bodyObj) throws IOException;

    /**
     * 序列化协议
     * @return
     */
    public abstract Protocol getProtocol();

    /**
     * 对象到字节数组
     * @param obj
     * @return
     */
    public abstract byte[] obj2Bytes(Object obj) throws IOException;

    /**
     * 字节数组到对象
     * @param bytes
     * @return
     * @throws IOException
     */
    public abstract Object bytes2Obj(byte[] bytes) throws IOException;

    /**
     * 头部大小
     * @return
     */
    public int getHeaderBitLen(){
        return getStr8BitLen() * 2 + PROTOCOL_LEN;
    }

    /**
     * 8位字符串大小
     * @return
     */
    public abstract int getStr8BitLen();


    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }
}