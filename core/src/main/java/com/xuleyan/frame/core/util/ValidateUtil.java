/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import com.xuleyan.frame.common.exception.CommonException;
import com.xuleyan.frame.core.ValidateResult;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;

/**
 * 参数校验，根据参数上的注解
 *
 * @author xuleyan
 * @version ValidateUtil.java, v 0.1 2018-06-08 14:50 xuleyan
 */
public class ValidateUtil {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 验证参数
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> ValidateResult validate(T object) {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setResult(true);
        //执行验证
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object);
        //如果有验证信息，则将第一个取出来包装成异常返回
        ConstraintViolation<T> constraintViolation = getFirst(constraintViolations, null);
        if (constraintViolation != null) {
            validateResult.setResult(false);
            validateResult.setParamName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, constraintViolation.getPropertyPath().toString()));
            validateResult.setMsg(constraintViolation.getMessage());
        }
        return validateResult;
    }

    /**
     * 验证参数
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> ValidateResult validateWithGroup(T object, Class<?>... classes) {
        ValidateResult validateResult = new ValidateResult();
        validateResult.setResult(true);
        //执行验证
        Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(object, classes);
        //如果有验证信息，则将第一个取出来包装成异常返回
        ConstraintViolation<T> constraintViolation = getFirst(constraintViolations, null);
        if (constraintViolation != null) {
            validateResult.setResult(false);
            validateResult.setParamName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, constraintViolation.getPropertyPath().toString()));
            validateResult.setMsg(constraintViolation.getMessage());
        }
        return validateResult;
    }

    /**
     * 验证参数，如果不符合抛出异常
     *
     * @param request
     * @param <T>
     * @return
     */
    public static <T> void validateWithThrow(T request) {
        // 参数校验
        ValidateResult validate = validate(request);
        if (!validate.isResult()) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance("【无效参数】参数名:{0},原因:{1}", validate.getParamName(), validate.getMsg());
        }
    }

    /**
     * 验证参数，如果不符合抛出异常
     *
     * @param request
     * @param <T>
     * @return
     */
    public static <T> void validateWithThrowAndGroup(T request, Class<?>... classes) {
        // 参数校验
        ValidateResult validate = validateWithGroup(request, classes);
        if (!validate.isResult()) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance("【无效参数】参数名:{0},原因:{1}", validate.getParamName(), validate.getMsg());
        }
    }

    /**
     * 自定义断言工具
     *
     * @param expression 断言表达式
     * @param message    模板字符串提示信息，占位符={}
     */
    public static void check(boolean expression, String message) {
        if (!expression) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance(message);
        }
    }

    /**
     * 自定义断言工具(支持模板字符串提示信息)
     *
     * @param expression 断言表达式
     * @param message    模板字符串提示信息，占位符={}
     * @param args       参数
     */
    public static void check(boolean expression, String message, Object... args) {
        if (!expression) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance(StrUtil.format(message, args));
        }
    }

    /**
     * 空对象断言工具
     *
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance(message);
        }
    }

    /**
     * 空字符串断言工具
     *
     * @param s
     * @param message
     */
    public static void notEmpty(CharSequence s, String message) {
        if (StringUtils.isEmpty(s)) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance(message);
        }
    }

    /**
     * 集合断言工具
     *
     * @param collection
     * @param message
     */
    public static void notEmpty(Collection collection, String message) {
        if (CollectionUtil.isEmpty(collection)) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance(message);
        }
    }

    /**
     * 空白字符串断言工具
     *
     * @param s
     * @param message
     */
    public static void notBlank(CharSequence s, String message) {
        if (StringUtils.isBlank(s)) {
            throw CommonException.INVALID_PARAM_ERROR.newInstance(message);
        }
    }
}


