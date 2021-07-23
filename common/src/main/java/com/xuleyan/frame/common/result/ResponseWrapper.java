/**
 * xuleyan.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.common.result;

import java.io.Serializable;

/**
 *
 * @author xuleyan
 * @version ResponseWrapper.java, v 0.1 2020-09-03 4:04 下午
 */
public class ResponseWrapper<T> implements Serializable {

    /**
     * 本次请求是否满足预期
     */
    private boolean success = true;

    /**
     * 请求数据结构体
     */
    private T data;

    /**
     * 错误码
     */
    private String errCode;

    /**
     * 错误提示信息
     */
    private String errMsg;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "success=" + success +
                ", data=" + data +
                ", errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }

}
