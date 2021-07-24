/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.inteceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuleyan.frame.tracer.configuration.TracerProperties;
import com.xuleyan.frame.tracer.inteceptor.TracingMethodInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.time.Clock;
import java.util.*;

/**
 * @author xuleyan
 * @version TracingRpcMethodInterceptor.java, v 0.1 2021-07-24 4:06 下午
 */
@Slf4j
public class TracingRpcMethodInterceptor extends TracingMethodInterceptor {

    public TracingRpcMethodInterceptor(TracerProperties tracerProperties) {
        super(tracerProperties);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // start Time = Clock.systemUTC().millis()
        long start = Clock.systemUTC().millis();

        // 判断rpcRequestLogOut 输出className + methodName + argumentList
        Method method = invocation.getMethod();
        Class<? extends MethodInvocation> aClass = invocation.getClass();

        String classAndMethodName = aClass.getName() + "." + method.getName() + "()";

        Object[] arguments = invocation.getArguments();
        if (tracerProperties.isRpcRequestLogOut()) {
            List<Object> argumentList = new ArrayList<>();
            Collections.addAll(argumentList, arguments);
            log.info("{}, requestParams: {}", classAndMethodName, JSON.toJSON(argumentList));
        }

        Object proceed = null;
        try {
            proceed = invocation.proceed();

        } finally {
            // 判断执行时间耗时
            long spendTime = Clock.systemUTC().millis() - start;
            if (tracerProperties.isRpcResponseTimeLogOut()) {
                log.info("{}, responseTime: {}", classAndMethodName, spendTime);
            }

            String response = "";
            if (proceed instanceof String) {
                response = proceed.toString();
            } else {
                try {
                    // WriteMapNullValue 让map中值为null的数据继续显示
                    // WriteNullListAsEmpty 对象中包含有List属性值为null的，显示为[]
                    // DisableCircularReferenceDetect 避免循环依赖
                    response = JSON.toJSONString(proceed, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect);
                } catch (Exception e) {
                    response = "JSON.toJSONString error";
                }
            }

            // 打印执行结果
            if (tracerProperties.isRpcResponseLogOut()) {
                log.info("{}, response: {}", classAndMethodName, response);
            }
        }
        return proceed;
    }

}