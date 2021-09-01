/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.common.enums;

/**
 *
 * @author xuleyan
 * @version DateFormatEnum.java, v 0.1 2021-08-22 8:15 下午
 */
public enum DateFormatEnum {
    CHINESE_DATE_12("yyyy年MM月dd日 hh时mm分ss秒", "2019年10月01日 02时22分38秒"),
    CHINESE_DATE_24("yyyy年MM月dd日 HH时mm分ss秒", "2019年10月01日 14时22分38秒"),
    CHINESE_DATE_12_WEEK("yyyy年MM月dd日 hh时mm分ss秒 E", "2019年10月01日 02时22分38秒 星期二"),
    CHINESE_DATE_24_WEEK("yyyy年MM月dd日 HH时mm分ss秒 E", "2019年10月01日 14时22分38秒 星期二"),
    YEAR_MONTH_DAY("yyyy-MM-dd", "2019-10-01"),
    YEAR_MONTH_DAY_12("yyyy-MM-dd hh:mm:ss", "2019-10-01 02:22:38"),
    YEAR_MONTH_DAY_24("yyyy-MM-dd HH:mm:ss", "2019-10-01 14:22:38"),
    YEAR_MONTH_DAY_12_SECOND("yyyy-MM-dd hh:mm:ss.SSS", "2019-10-01 02:22:38.855"),
    YEAR_MONTH_DAY_24_SECOND("yyyy-MM-dd HH:mm:ss.SSS", "2019-10-01 14:22:38.855"),
    YMD("yyyyMMdd", "20191001"),
    YMD_SLASH("yyyy/MM/dd", "2019/10/01"),
    YMD_STRING_WITH_SECOND_12("yyyyMMddhhmmss", "20191001142238"),
    YMD_STRING_WITH_SECOND_24("yyyyMMddHHmmss", "20191001142238"),
    YMD_STRING_WITH_SECOND_MS_12("yyyyMMddhhmmssSSS", "20191001142238855"),
    YMD_STRING_WITH_SECOND_MS_24("yyyyMMddHHmmssSSS", "20191001142238855");

    private final String format;

    private final String desc;

    DateFormatEnum(String format, String desc) {
        this.format = format;
        this.desc = desc;
    }

    public String getFormat() {
        return format;
    }

    public String getDesc() {
        return desc;
    }
}