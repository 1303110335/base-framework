/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.inteceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializableSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xuleyan.frame.tracer.configuration.TracerProperties;
import com.xuleyan.frame.tracer.configuration.TracerSpringMvcProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Clock;
import java.util.*;

/**
 * @author xuleyan
 * @version TracingMvcMethodInterceptor.java, v 0.1 2021-07-24 4:52 下午
 */
@Slf4j
public class TracingMvcMethodInterceptor extends TracingMethodInterceptor {

    /**
     * 包含日志 URL
     */
    private final Set<String> includeUrlSet = new HashSet<>();

    /**
     * 排除日志 URL
     */
    private final Set<String> excludeUrlSet = new HashSet<>();

    public TracingMvcMethodInterceptor(TracerProperties tracerProperties) {
        super(tracerProperties);

        TracerSpringMvcProperties springMvcProperties = tracerProperties.getSpringMvcProperties();
        if (null == springMvcProperties) {
            return;
        }

        String includeUrls = springMvcProperties.getIncludeUrls();
        if (Objects.nonNull(includeUrls)) {
            includeUrlSet.addAll(Arrays.asList(includeUrls.split(",")));
        }

        String excludeUrls = springMvcProperties.getExcludeUrls();
        if (Objects.nonNull(excludeUrls)) {
            String[] excludeUrlArray = excludeUrls.split(",");
            for (String excludeUrl : excludeUrlArray) {
                if ("".equals(excludeUrl)) {
                    continue;
                }

                if (includeUrlSet.contains(excludeUrl)) {
                    throw new IllegalArgumentException(String.format("includeUrl 中已包含 %s uri, ", excludeUrl));
                } else {
                    excludeUrlSet.add(excludeUrl);
                }
            }
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 开始时间
        long start = Clock.systemUTC().millis();
        // 获取 clazz 和 method
        Method method = invocation.getMethod();
        Class<? extends MethodInvocation> clazz = invocation.getClass();

        // 判断是否是request请求, get,post,delete,options,request, 若不是则返回
        if (!isHttpRequest(method)) {
            return invocation.proceed();
        }

        // 获取httpServletRequest
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }

        // arguments参数处理
        Object[] arguments = invocation.getArguments();
        List<Object> argumentList = new ArrayList<>();
        if (arguments != null && arguments.length > 0) {
            for (Object argument : arguments) {
                if (!(argument instanceof HttpServletRequest) && !(argument instanceof HttpServletResponse)) {
                    argumentList.add(argument);
                }
            }
        }

        String classAndMethodName = clazz.getName() + "." + method.getName() + "()";
        // 判断urlLogout是否是true，若是则打印日志
        boolean urlLogOut = false;
        if (null != request) {
            String uri = request.getRequestURI();
            urlLogOut = (includeUrlSet.size() == 0 || includeUrlSet.contains(uri)) && !excludeUrlSet.contains(uri);

            if (tracerProperties.isMvcRequestLogOut()) {
                if (urlLogOut) {
                    log.info("{}, requestParams: {}, requestUri: {}, requestHeaders: {}",
                            classAndMethodName, JSON.toJSONString(argumentList), request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
                }
            }
        }

        // 代理执行
        Object proceed = null;
        try {
            proceed = invocation.proceed();
        } finally {
            long spendTime = Clock.systemUTC().millis() - start;
            // 判断isMvcResponseLogOut,输出响应出参和headers
            String response;
            if (proceed instanceof String) {
                response = proceed.toString();
            } else {
                try {
                    response = JSON.toJSONString(proceed, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect);
                } catch (Exception e) {
                    response = "JSON.toJSONString Error";
                }
            }
            if (tracerProperties.isMvcResponseLogOut() && urlLogOut) {
                HttpServletResponse servletResponse = servletRequestAttributes.getResponse();
                if (servletResponse != null) {
                    Collection<String> headerNames = servletResponse.getHeaderNames();
                    Map<String, String> headersMap = new HashMap<>(headerNames.size() * 4 / 3 + 1);
                    headerNames.forEach(headerKey -> headersMap.put(headerKey, servletResponse.getHeader(headerKey)));
                    log.info("{}, response = {}, responseHeaders = {}", classAndMethodName, response, headersMap);
                }
            }

            // 判断isMvcResponseTimeLogOut，输出执行时间
            if (tracerProperties.isMvcResponseTimeLogOut() && urlLogOut) {
                log.info("{}, millisecond(millisecond): {}", classAndMethodName, spendTime);
            }
        }

        return proceed;
    }

    /**
     * 判断是否是Http请求
     *
     * @param method
     * @return
     */
    private boolean isHttpRequest(Method method) {
        return (method.isAnnotationPresent(RequestMapping.class) || method.isAnnotationPresent(PostMapping.class) ||
                method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(DeleteMapping.class));
    }
}