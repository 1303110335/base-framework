/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.core;

/**
 * 校验结果
 * @author xuleyan
 * @version ValidateResult.java, v 0.1 2018-08-28 23:37 xuleyan
 */
public class ValidateResult {
    private boolean result;
    private String paramName;
    private String msg;

    /**
     * Getter method for property <tt>result</tt>.
     *
     * @return property value of result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Setter method for property <tt>result</tt>.
     *
     * @param result value to be assigned to property result
     */
    public void setResult(boolean result) {
        this.result = result;
    }

    /**
     * Getter method for property <tt>paramName</tt>.
     *
     * @return property value of paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Setter method for property <tt>paramName</tt>.
     *
     * @param paramName value to be assigned to property paramName
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * Getter method for property <tt>msg</tt>.
     *
     * @return property value of msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Setter method for property <tt>msg</tt>.
     *
     * @param msg value to be assigned to property msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}