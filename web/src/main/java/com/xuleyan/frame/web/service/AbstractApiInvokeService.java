/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.web.service;

import com.xuleyan.frame.core.BaseParam;
import com.xuleyan.frame.web.bean.ApplicationContextHelper;
import com.xuleyan.frame.web.domain.ApiContainer;
import com.xuleyan.frame.web.domain.ApiDescriptor;
import com.xuleyan.frame.web.exception.ApiInvokeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xuleyan
 * @version AbstractApiInvokeService.java, v 0.1 2020-06-01 10:04 AM xuleyan
 */
@Component
public abstract class AbstractApiInvokeService {

    @Autowired
    protected ApiClient apiClient;

    @Autowired
    protected ApiContainer apiContainer;

    public <P extends BaseParam, R> R invoke(String appId, String sign, String apiMethodName, Map<String, Object> params, String content) throws Throwable {

        ApiDescriptor apiDescriptor = apiContainer.get(apiMethodName);
        if (apiDescriptor == null) {
            throw ApiInvokeException.API_NOT_EXIST;
        }
        String beanName = apiDescriptor.getBeanName();
        Object bean = ApplicationContextHelper.getBean(beanName);
        if (null == bean) {
            throw ApiInvokeException.API_NOT_EXIST;
        }
        Boolean hasPermission = checkPermission(appId, apiMethodName);
        if (!hasPermission) {
            throw ApiInvokeException.DO_NOT_HAS_PERMISSION;
        }
        Boolean checkSign = checkSign(params, appId);
        if (!checkSign) {
            throw ApiInvokeException.API_INVALID_SIGIN;
        }
        return apiClient.invoke(apiMethodName, content, (ApiInvoker<P, R>) (method, param) -> (R) apiDescriptor.getMethod().invoke(bean, param));

    }

    protected abstract Boolean checkSign(Map<String, Object> params, String appId);

    protected abstract Boolean checkPermission(String appId, String apiMethodName);
}