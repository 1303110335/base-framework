/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.common.enums;

/**
 *
 * @author xuleyan
 * @version DateEnum.java, v 0.1 2021-08-22 8:23 下午
 */
public enum DateEnum {

    /** 秒 */
    SECOND(13),
    /** 分钟 */
    MINUTE(12),
    /** 小时 */
    HOUR(11),
    /** 天 */
    DAY(6),
    /** 月 */
    MONTH(2),

    ;

    private final int code;

    DateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}