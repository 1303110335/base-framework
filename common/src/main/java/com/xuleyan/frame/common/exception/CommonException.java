/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.common.exception;



import com.xuleyan.frame.common.enums.CommonErrorEnum;

import java.io.Serializable;
import java.text.MessageFormat;

public class CommonException extends BaseException implements Serializable {

    /**
     * 系统异常
     */
    public static final CommonException SYSTEM_ERROR = new CommonException(CommonErrorEnum.SYS_ERROR);
    /**
     * 未发现当前系统运行环境异常
     */
    public static final CommonException NO_FIND_CURRENT_RUNTIME_ENV_ERROR = new CommonException(CommonErrorEnum.NO_FIND_CURRENT_RUNTIME_ENV);
    /**
     * 参数不正确
     */
    public static final CommonException INVALID_PARAM_ERROR = new CommonException(CommonErrorEnum.INVALID_PARAM);
    private static final long serialVersionUID = 308187765403533954L;


    public CommonException() {
    }

    /**
     * 异常构造器
     *
     * @param code
     * @param msg
     */
    private CommonException(String code, String msg) {
        super(code,msg);
    }

    private CommonException(CommonErrorEnum commonErrorEnum) {
        this(commonErrorEnum.getValue(), commonErrorEnum.getName());
    }

    @Override
    public CommonException newInstance(String msgFormat, Object... args) {
        return new CommonException(this.code, MessageFormat.format(msgFormat, args));
    }
}