/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.web.exception;



import com.xuleyan.frame.common.enums.CommonErrorEnum;
import com.xuleyan.frame.common.exception.BaseException;

import java.text.MessageFormat;

/**
 * Api反射调用公共异常
 *
 * @author wujn
 * @version ApiInvokeException.java, v 0.1 2018-06-11 16:32
 */
public class ApiInvokeException extends BaseException {
    /**
     * API不存在
     */
    public static final ApiInvokeException API_NOT_EXIST = new ApiInvokeException(CommonErrorEnum.API_NOT_EXIST);

    /**
     * 参数个数不匹配
     */
    public static final ApiInvokeException API_PARAM_LENGTH_ERROR = new ApiInvokeException(CommonErrorEnum.INVALID_PARAM);

    /**
     * 没有权限
     */
    public static final ApiInvokeException DO_NOT_HAS_PERMISSION = new ApiInvokeException(CommonErrorEnum.PERMISSION_DENIED);
    /**
     * 参数验证异常
     */
    public static final ApiInvokeException API_PARAM_VALIDATE_ERROR = new ApiInvokeException(CommonErrorEnum.PARAM_VALIDATE_ERROR);
    /**
     * 签名验证异常
     */
    public static final ApiInvokeException API_INVALID_SIGIN = new ApiInvokeException(CommonErrorEnum.SIGN_ERROR);
    /**
     * API调用异常
     */
    public static final ApiInvokeException API_INVOKE_ERROR = new ApiInvokeException(CommonErrorEnum.SYS_ERROR);


    public ApiInvokeException() {
    }

    /**
     * 异常构造器
     */
    private ApiInvokeException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 异常构造器
     *
     * @param commonErrorEnum
     */
    private ApiInvokeException(CommonErrorEnum commonErrorEnum) {
        this(commonErrorEnum.getValue(), commonErrorEnum.getName());
    }

    /**
     * 实例化异常
     *
     * @param msgFormat
     * @param args
     * @return 异常类
     */
    @Override
    public ApiInvokeException newInstance(String msgFormat, Object... args) {
        return new ApiInvokeException(this.code, MessageFormat.format(msgFormat, args));
    }
}
