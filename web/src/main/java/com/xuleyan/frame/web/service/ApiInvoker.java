/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.web.service;


import com.xuleyan.frame.core.BaseParam;
import com.xuleyan.frame.web.domain.ApiDescriptor;

import java.lang.reflect.InvocationTargetException;

/**
 * @author xuleyan
 * @version ApiInvoker.java, v 0.1 2018-06-08 14:39
 */
public interface ApiInvoker<P extends BaseParam, R> {

    /**
     * API调用接口
     *
     * @param apiDescriptor
     * @param param
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    R invoke(ApiDescriptor apiDescriptor, P param) throws InvocationTargetException, IllegalAccessException;

}