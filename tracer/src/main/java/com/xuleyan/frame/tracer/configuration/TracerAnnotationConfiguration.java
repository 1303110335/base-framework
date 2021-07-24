/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.configuration;

import com.xuleyan.frame.tracer.inteceptor.TracingMethodInterceptor;
import com.xuleyan.frame.tracer.inteceptor.TracingMvcMethodInterceptor;
import com.xuleyan.frame.tracer.inteceptor.TracingRpcMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

/**
 *
 * @author xuleyan
 * @version TracerAnnotationConfiguration.java, v 0.1 2021-07-24 12:17 下午
 */
@Slf4j
public class TracerAnnotationConfiguration {

    @Bean
    @ConditionalOnClass(name = {"org.apache.dubbo.config.annotation.Service"})
    @ConditionalOnProperty(prefix = "com.xuleyan.tracer", name = {"global-log-out", "rpc-log-out"}, matchIfMissing = true)
    public DefaultPointcutAdvisor tracerRpc(TracerProperties tracerProperties) {
        // 日志:trace rpc启用
        log.info("trace rpc 启用 >> tracerProperties = {}", tracerProperties);
        // 创建一个TracingMethodInterceptor
        TracingMethodInterceptor methodInterceptor = new TracingRpcMethodInterceptor(tracerProperties);
        // 创建一个pointcut
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(Service.class, true);
        // 创建一个DefaultPointcutAdvisor
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(pointcut);
        defaultPointcutAdvisor.setAdvice(methodInterceptor);
        return defaultPointcutAdvisor;
    }

    @Bean
    @ConditionalOnClass(name = {"org.springframework.stereotype.Controller"})
    @ConditionalOnProperty(prefix = "com.xuleyan.tracer", name = {"global-log-out", "rpc-log-out"}, matchIfMissing = true)
    public DefaultPointcutAdvisor tracerMvc(TracerProperties tracerProperties) {
        log.info("trace mvc 启用 >> tracerProperties = {}", tracerProperties);
        TracingMethodInterceptor methodInterceptor = new TracingMvcMethodInterceptor(tracerProperties);
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(Controller.class, true);
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
        defaultPointcutAdvisor.setPointcut(pointcut);
        defaultPointcutAdvisor.setAdvice(methodInterceptor);
        return defaultPointcutAdvisor;
    }

}