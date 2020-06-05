/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.web.bean;

import com.xuleyan.frame.core.annotation.XuApi;
import com.xuleyan.frame.web.contants.ApiConstants;
import com.xuleyan.frame.web.domain.ApiContainer;
import com.xuleyan.frame.web.domain.ApiDescriptor;
import com.xuleyan.frame.web.exception.ApiInvokeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author xuleyan
 * @version ApiScanProcessor.java, v 0.1 2020-06-01 12:00 PM xuleyan
 */
@Component
public class ApiScanProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiScanProcessor.class);

    @Resource
    private ApiContainer apiContainer;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Method[] declaredMethods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        for (Method method : declaredMethods) {
            // 找到有 OpenApi 注解的方法
            XuApi xuApi = AnnotationUtils.findAnnotation(method, XuApi.class);
            if (xuApi != null) {
                // 检测方法是否合法
                // 校验参数个数
                int parameterCount = method.getParameterCount();
                if (ApiConstants.DEFAULT_API_PARAM_LENGTH > parameterCount) {
                    String errorMsg = MessageFormat.format("【API加载异常】方法 method = {0}, 注解 @XuApi 不合法, 参数个数小于1", method.toGenericString());
                    LOGGER.error(errorMsg);
                    throw ApiInvokeException.API_PARAM_VALIDATE_ERROR.newInstance(errorMsg);
                }
                String name = xuApi.name();
                ApiDescriptor apiDescriptor = null;
                try {
                    apiDescriptor = new ApiDescriptor(method.toGenericString(), name, beanName);
                } catch (Exception ex) {
                    String msg = MessageFormat.format("【API加载异常】,msg={0}", ex.getMessage());
                    LOGGER.error(msg);
                    throw ApiInvokeException.API_PARAM_VALIDATE_ERROR.newInstance(msg);
                }
                apiContainer.put(name, apiDescriptor);
                LOGGER.info("已加载API,name={},api={}", name, apiDescriptor);
            }
        }
        return bean;
    }
}