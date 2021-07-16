/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.rpc;

import java.io.Serializable;

/**
 *
 * @author xuleyan
 * @version RpcResult.java, v 0.1 2021-07-08 9:37 下午
 */
public class RpcResult implements Serializable {

    private boolean success;

    private Object data;

    private Throwable e;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public <T> T getData() {
        return (T)data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        this.e = e;
    }


    public static RpcResult instance(Throwable e){
        RpcResult result = new RpcResult();
        result.setE(e);
        result.setSuccess(false);
        return result;
    }

    public static RpcResult instance(Object data){
        RpcResult result = new RpcResult();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }
}