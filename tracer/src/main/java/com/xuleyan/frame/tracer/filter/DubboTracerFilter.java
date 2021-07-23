/**
 * bianque.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.filter;


import com.xuleyan.frame.tracer.utils.TraceIdGeneratorUtils;
import com.xuleyan.frame.tracer.utils.TracerContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 *
 * @author xuleyan
 * @version DubboTracerFilter.java, v 0.1 2021-07-23 9:03 下午
 */
@Activate(group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER}, value = "dubboTracerFilter", order = 1)
public class DubboTracerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 获取rpcContext
        RpcContext context = RpcContext.getContext();
        // 获取rpcTraceId
        String rpcTraceId = context.getAttachment("rpcTraceId");
        // 获取side
        String side = context.getUrl().getParameter(CommonConstants.SIDE_KEY);
        // 判断rpcTraceId是否为空
        if (StringUtils.isBlank(rpcTraceId)) {
            // 若为空则
            // 1.判断side是否为"consumer"，若是则将MDC中的traceId赋给rpcTraceId
            if (CommonConstants.CONSUMER_SIDE.equals(side)) {
                rpcTraceId = MDC.get("traceId");
            }
            // 2.判断是否仍旧为空，若是则用TraceIdGeneratorUtils重新生成一个，可以先按照remoteHost生成
            if (StringUtils.isBlank(rpcTraceId)) {
                String remoteHost = context.getRemoteHost();
                if (StringUtils.isNotBlank(remoteHost)) {
                    rpcTraceId = TraceIdGeneratorUtils.getTraceId(remoteHost);
                } else {
                    rpcTraceId = TraceIdGeneratorUtils.generate();
                }
            }
            // 3.将rpcTraceId重新放入rpcContext
            context.setAttachment("rpcTraceId", rpcTraceId);
        }
        // 判断side是否为provider，若是则将rpcTraceId放入到TracerContextUtils的threadLocal中
        if (CommonConstants.PROVIDER.equals(side)) {
            TracerContextUtils.setTracerId(rpcTraceId);
        }

        // 调用invoker.invoke方法继续
        return invoker.invoke(invocation);
    }
}