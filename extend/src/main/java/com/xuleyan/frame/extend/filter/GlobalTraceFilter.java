/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.extend.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.xuleyan.frame.common.annotation.NoGlobalLog;
import com.xuleyan.frame.common.exception.BaseException;
import com.xuleyan.frame.common.exception.CommonException;
import com.xuleyan.frame.core.util.LogUtil;
import com.xuleyan.frame.core.util.SystemClock;
import com.xuleyan.frame.core.util.TraceIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.lang.reflect.Method;

/**
 * dubbo 全局 trace 过滤器
 * @author xuleyan
 * @version GlobalTraceFilter.java, v 0.1 2020-05-29 10:12 AM xuleyan
 */
@Slf4j
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class GlobalTraceFilter implements Filter {

    public static final String TRACE_ID = "TRACE_ID";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String mdcTraceId = invocation.getAttachment(TRACE_ID);
        // 获得 mdc 上下文的 tradeId
        boolean isProvider = RpcContext.getContext().isProviderSide();
        if (isProvider) {
            if (StringUtils.isBlank(mdcTraceId)) {
                mdcTraceId = TraceIdGenerator.generate();
            }
            RpcContext.getContext().setAttachment(TRACE_ID, mdcTraceId);
        }
        if (!isProvider) {
            if (StringUtils.isBlank(mdcTraceId)) {
                mdcTraceId = MDC.get(TRACE_ID);
            }
            if (StringUtils.isBlank(mdcTraceId)) {
                mdcTraceId = TraceIdGenerator.generate();
            }
            RpcContext.getContext().setAttachment(TRACE_ID, mdcTraceId);
        }
        // 把 traceId 放入日志的上下文中
        MDC.put(TRACE_ID, mdcTraceId);

        // 获取RPC方法名
        String methodName = invoker.getUrl().getPath();
        // 判断是否记录调用日志
        boolean isLog = false;
        try {
            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            NoGlobalLog noGlobalLog = method.getAnnotation(NoGlobalLog.class);
            isLog = noGlobalLog == null;
            methodName += "." + method.getName();

        } catch (NoSuchMethodException e) {
            LogUtil.error(log, "方法: invoke 发生异常， 参数: invoker = {}, invocation = {} ,异常: Ex = {}", invoker, invocation, e);
        }

        // 获得开始时间
        long startTime = SystemClock.millisClock().now();

        // 打印调用前日志
        if (isLog) {
            Object[] arguments = invocation.getArguments();
            LogUtil.info(log, "RPC 接口开始 methodName = {}, agruments = {}", methodName, arguments);
        }

        // 调用接口
        Result result = invoker.invoke(invocation);

        // 打印调用后日志
        if (isLog) {
            // 抛出的异常
            Throwable exception = result.getException();
            // 返回结果
            Object value = result.getValue();
            // 打印结束日志
            if (exception != null) {
                LogUtil.error(log, "RPC 接口异常结束 methodName = {}, time = {}ms ", exception, methodName, SystemClock.millisClock().now() - startTime);
                // 如果不是服务自定义异常，就返回系统错误
                if (!(exception instanceof BaseException)) {
                    LogUtil.error(log, "RPC 接口异常结束 methodName = {},  exception = {}, time = {}ms ", methodName, exception, SystemClock.millisClock().now() - startTime);
                    if (isProvider) {
                        // 清空 MDC
                        MDC.clear();
                    }
                    return new RpcResult(CommonException.SYSTEM_ERROR);
                }

            } else {
                LogUtil.info(log, "RPC 接口结束 methodName = {},  result ={}, time = {}ms ", methodName, value, SystemClock.millisClock().now() - startTime);
            }
        }

        if (isProvider) {
            // 清空 MDC
            MDC.clear();
        }
        return result;
    }
}