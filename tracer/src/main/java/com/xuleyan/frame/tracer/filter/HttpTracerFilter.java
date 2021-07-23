/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.tracer.filter;

import com.xuleyan.frame.tracer.utils.TraceIdGeneratorUtils;
import com.xuleyan.frame.tracer.utils.TracerContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author xuleyan
 * @version HttpTracerFilter.java, v 0.1 2021-07-23 3:18 下午
 */
@WebFilter(urlPatterns = "/**")
@Order(Ordered.HIGHEST_PRECEDENCE + 1024)
@Slf4j
public class HttpTracerFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        /* 结合目前的追踪服务, 由前端开始传值, 或者第三方接口调用者 */

        String requestId = servletRequest.getHeader("RequestId");
        if (null == requestId) {
            requestId = TraceIdGeneratorUtils.getTraceId(servletRequest);
        }

        TracerContextUtils.setTracerId(requestId);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            log.error("HttpTracerFilter doFilter异常", e);
        } finally {
            TracerContextUtils.clear();
        }

    }

    @Override
    public void destroy() {

    }
}