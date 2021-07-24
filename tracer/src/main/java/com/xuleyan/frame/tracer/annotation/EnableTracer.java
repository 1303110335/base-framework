/**
 * xuleyan.com
 * Copyright (C) 2013-2021All Rights Reserved.
 */
package com.xuleyan.frame.tracer.annotation;

import com.xuleyan.frame.tracer.configuration.TracerAnnotationConfiguration;
import com.xuleyan.frame.tracer.configuration.TracerProperties;
import com.xuleyan.frame.tracer.filter.HttpTracerFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 *
 * @author xuleyan
 * @version EnableTracer.java, v 0.1 2021-07-24 11:08 上午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HttpTracerFilter.class, TracerProperties.class, TracerAnnotationConfiguration.class})
public @interface EnableTracer {

}