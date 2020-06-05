/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.web.service;


import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.xuleyan.frame.common.exception.BaseException;
import com.xuleyan.frame.core.BaseParam;
import com.xuleyan.frame.core.ValidateResult;
import com.xuleyan.frame.core.util.LogUtil;
import com.xuleyan.frame.core.util.SystemClock;
import com.xuleyan.frame.core.util.TraceIdGenerator;
import com.xuleyan.frame.core.util.ValidateUtil;
import com.xuleyan.frame.web.contants.ApiConstants;
import com.xuleyan.frame.web.domain.ApiContainer;
import com.xuleyan.frame.web.domain.ApiDescriptor;
import com.xuleyan.frame.web.exception.ApiInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

/**
 * @author buhao
 * @version ApiClient.java, v 0.1 2018-06-08 14:50 buhao
 */
@Component
@Slf4j
public class ApiClient {
    /**
     * 设置FASTJSON解析使用下划线的方式
     */
    private static final ParserConfig FAST_JSON_PARSER_CONFIG = new ParserConfig();
    private static final String  ERROR_INFO = "Http 接口异常结束 methodName = {},  exception = {}, time = {}ms";
    static {
        FAST_JSON_PARSER_CONFIG.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    @Autowired
    private ApiContainer apiContainer;

    /**
     * API调用方法
     *
     * @param method
     * @param paramJsonStr
     * @param invoker
     * @param <P>
     * @param <R>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <P extends BaseParam, R> R invoke(String method, String paramJsonStr, ApiInvoker<P, R> invoker) throws Throwable {

        // 获得开始时间
        long start = SystemClock.millisClock().now();
        // 生成 taceId
        String traceId = TraceIdGenerator.generate();
        // 将 traceId 放入日志上下文
        MDC.put("TRACE_ID", traceId);
        // 将 traceId 放入 dubbo (附件)上下文
        RpcContext.getContext().setAttachment("TRACE_ID", traceId);
        LogUtil.info(log, "Http 接口开始 methodName = {}, agruments = {}", method, paramJsonStr);


        //获得方法信息
        ApiDescriptor apiDescriptor = apiContainer.get(method);
        try {
            // 序列化方法参数
            String[] paramFullNameList = apiDescriptor.getParamFullNameList();
            if (paramFullNameList == null || paramFullNameList.length < ApiConstants.DEFAULT_API_PARAM_LENGTH) {
                throw ApiInvokeException.API_PARAM_LENGTH_ERROR;
            }
            P result = JSON.parseObject(paramJsonStr, Class.forName(paramFullNameList[0]), FAST_JSON_PARSER_CONFIG);
            // 校验参数
            ValidateResult validateResult = ValidateUtil.validate(result);
            if (!validateResult.isResult()) {
                throw ApiInvokeException.API_PARAM_VALIDATE_ERROR.newInstance("参数{0}{1}", validateResult.getParamName(), validateResult.getMsg());
            }
            // 调用方法
            R invoke = invoker.invoke(apiDescriptor, result);

            LogUtil.info(log, "Http 接口结束 methodName = {}, result = {}, time = {}ms", method, invoke, SystemClock.millisClock().now() - start);

            return invoke;
        } catch (ApiInvokeException e) {
            printInfoLog(method, start, e);
            throw e;
        } catch (BaseException e) {
            printInfoLog(method, start,  e);
            throw e;
        } catch (InvocationTargetException e){
            printInfoLog(method, start,  e);
            throw e.getTargetException();
        }catch (Exception e) {
            printInfoLog(method, start,  e);
            String msg = MessageFormat.format("API调用异常,msg={0}", e.getMessage());
            LogUtil.error(log, msg, e);
            throw ApiInvokeException.API_INVOKE_ERROR;
        }
    }

    private void printInfoLog(String method, long start, Exception e) {
        LogUtil.info(log, ERROR_INFO, method, e, SystemClock.millisClock().now() - start);
    }
}