/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.configuration;

/**
 *
 * @author xuleyan
 * @version TracerSpringMvcProperties.java, v 0.1 2021-07-24 12:16 下午
 */
public class TracerSpringMvcProperties {

    /**
     * 包含 URL 默认 /** 拦截所有 多个使用 , 分割
     */
    private String includeUrls;

    /**
     * 排除 URL 多个使用 , 分割
     */
    private String excludeUrls;

    public String getIncludeUrls() {
        return includeUrls;
    }

    public void setIncludeUrls(String includeUrls) {
        this.includeUrls = includeUrls;
    }

    public String getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(String excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public String toString() {
        return "TracerSpringMvcProperties{" +
                "includeUrls='" + includeUrls + '\'' +
                ", excludeUrls='" + excludeUrls + '\'' +
                '}';
    }
}