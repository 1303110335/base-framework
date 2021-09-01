/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.common.util;

import com.alibaba.fastjson.JSON;
import com.xuleyan.frame.common.enums.DateFormatEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.regex.Pattern;

/**
 *
 * @author xuleyan
 * @version ValidateUtils.java, v 0.1 2021-08-22 8:18 下午
 */
public class ValidateUtils {
    private ValidateUtils() {
    }

    /**
     * 邮箱
     */
    public static final String EMAIL = "^[a-z0-9]([a-z0-9]*[-_\\.\\+]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z]{2,3}([\\.][a-z]{2,4})?$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL);

    /**
     * 汉字
     */
    public static final String CHINA_TEXT = "[\\u4e00-\\u9fa5]+";
    private static final Pattern CHINA_TEXT_PATTERN = Pattern.compile(CHINA_TEXT);

    /**
     * 中国姓名
     */
    public static final String CHINA_NAME = "[\\u4E00-\\u9FA5\\uf900-\\ufa2d·s]{2,20}";
    private static final Pattern CHINA_NAME_PATTERN = Pattern.compile(CHINA_NAME);


    /**
     * 手机号 简单校验 1字头＋10位数字即可
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    private static final Pattern PATTERN_REGEX_MOBILE_SIMPLE = Pattern.compile(REGEX_MOBILE_SIMPLE);


    /**
     * 手机号 电信
     */
    public static final String PHONE_CHINA_TELECOM = "^1[3578][01379]\\d{8}$";
    private static final Pattern PHONE_CHINA_TELECOM_PATTERN = Pattern.compile(PHONE_CHINA_TELECOM);

    /**
     * 手机号 联通
     */
    public static final String PHONE_CHINA_UNION = "^1[34578][01256]\\d{8}$";
    private static final Pattern PHONE_CHINA_UNION_PATTERN = Pattern.compile(PHONE_CHINA_UNION);

    /**
     * 手机号 电信
     */
    public static final String PHONE_CHINA_MOBILE = "^(134[012345678]\\d{7}|1[34578][012356789]\\d{8})$";
    private static final Pattern PHONE_CHINA_MOBILE_PATTERN = Pattern.compile(PHONE_CHINA_MOBILE);

    /**
     * 正则：IP地址 目前只支持 IPV4
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);

    /**
     * 网络协议
     */
    private static final String[] SCHEMES = {"http", "https"};

    /**
     * 时间格式 yyyy-MM-dd
     */
    public static final String REGEX_YEAR_MONTH_DAY = "((\\d{2}(([02468][048])|([13579][26]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-/\\s]?((((0?[13578])|(1[02]))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    private static final Pattern REGEX_YEAR_MONTH_DAY_PATTERN = Pattern.compile(REGEX_YEAR_MONTH_DAY);


    /**
     * 时间格式 yyyy-MM-dd hh:mm:ss
     */
    public static final String REGEX_YEAR_MONTH_DAY_H_M_S_12 = REGEX_YEAR_MONTH_DAY + "(\\s(((0[0-9])|([1][0-2])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9]))?)))?";
    private static final Pattern REGEX_YEAR_MONTH_DAY_H_M_S_12_PATTERN = Pattern.compile(REGEX_YEAR_MONTH_DAY_H_M_S_12);


    /**
     * 时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String REGEX_YEAR_MONTH_DAY_H_M_S_24 = REGEX_YEAR_MONTH_DAY + "(\\s(((0[0-9])|([1-2][0-3])):([0-5]?[0-9])((\\s)|(:([0-5]?[0-9]))?)))?";
    private static final Pattern REGEX_YEAR_MONTH_DAY_H_M_S_24_PATTERN = Pattern.compile(REGEX_YEAR_MONTH_DAY_H_M_S_24);


    /**
     * 验证是否为邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }


    /**
     * 验证字符串是否为中文
     *
     * @param chineseText
     * @return
     */
    public static boolean isChineseText(String chineseText) {
        if (StringUtils.isBlank(chineseText)) {
            return false;
        }
        return CHINA_TEXT_PATTERN.matcher(chineseText).matches();
    }


    /**
     * 验证字符串是否为中国人姓名
     * 包括少数民族      噶及·洛克业
     * 外国人翻译为中文  洛克·哈德森
     *
     * @param chineseName
     * @return
     */
    public static boolean isChineseName(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            return false;
        }
        return CHINA_NAME_PATTERN.matcher(chineseName).matches();
    }


    /**
     * 验证字符串是否为标准URL
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return false;
        }

        /* 原生框架不支持 localhost 校验, 这里人为处理 */
        url = StringUtils.replace(url, "localhost", "127.0.0.1");

        UrlValidator urlValidator = new UrlValidator(SCHEMES);
        return urlValidator.isValid(url);
    }


    /**
     * 验证字符串是否为标准URL
     *
     * @param url
     * @return
     */
    public static boolean isNotUrl(String url) {
        return !isUrl(url);
    }


    /**
     * 验证字符串是否为标准JSON字符串
     *
     * @param jsonStr
     * @return
     */
    public static boolean isJSON(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }
        return JSON.isValid(jsonStr);
    }


    /**
     * 验证字符串是否为标准JSON字符串
     *
     * @param jsonStr
     * @return
     */
    public static boolean isNotJSON(String jsonStr) {
        return !isJSON(jsonStr);
    }


    /**
     * 验证中国手机号 （精确）
     *
     * @param phone
     * @return
     * @apiNote 手机号更新比较频繁, 此API无法确保一直可用,若失效请联系开发负责人
     */
    @Deprecated
    public static boolean isMobileExact(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_CHINA_TELECOM_PATTERN.matcher(phone).matches() ||
                PHONE_CHINA_UNION_PATTERN.matcher(phone).matches() ||
                PHONE_CHINA_MOBILE_PATTERN.matcher(phone).matches();
    }


    /**
     * 验证手机号（简单）
     *
     * @param phone
     * @return
     */
    public static boolean isMobileSimple(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PATTERN_REGEX_MOBILE_SIMPLE.matcher(phone).matches();
    }


    /**
     * 验证IP地址 目前只支持 IPV4
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIP(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return false;
        }
        return PATTERN_REGEX_IP.matcher(ipAddress).matches();
    }


    /**
     * 验证时间是否合法
     * yyyy-MM-dd          2019-1-11 2019-01-11
     * yyyy-MM-dd HH:mm:ss 2019-10-22 01:11:11 true 2019-10-22 1:11:11  false
     * yyyy-MM-dd hh:mm:ss 2019-10-22 01:11:11 true 2019-10-22 13:11:11 false
     *
     * @param datetime
     * @param DateFormatEnum 目前只支持两种格式校验:  yyyy-MM-dd yyyy-MM-dd hh:mm:ss yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean isLegalDate(String datetime, DateFormatEnum DateFormatEnum) {
        if (StringUtils.isBlank(datetime)) {
            return false;
        }

        if (null == DateFormatEnum) {
            throw new NullPointerException("DateFormatEnum 不能为null");
        }

        switch (DateFormatEnum) {
            case YEAR_MONTH_DAY:
                return REGEX_YEAR_MONTH_DAY_PATTERN.matcher(datetime).matches();
            case YEAR_MONTH_DAY_12:
                return REGEX_YEAR_MONTH_DAY_H_M_S_12_PATTERN.matcher(datetime).matches();
            case YEAR_MONTH_DAY_24:
                return REGEX_YEAR_MONTH_DAY_H_M_S_24_PATTERN.matcher(datetime).matches();
            default:
                return false;
        }
    }


    /**
     * 是否有效身份证 只支持 18位
     *
     * @param idCard
     * @return
     */
    public static boolean isValidIDCard(String idCard) {
        return IDCardUtils.isLegalCardNo18(idCard);
    }

}