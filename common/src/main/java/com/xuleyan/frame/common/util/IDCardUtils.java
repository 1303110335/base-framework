/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Year;

/**
 *
 * @author xuleyan
 * @version IDCardUtils.java, v 0.1 2021-08-22 8:22 下午
 */
public class IDCardUtils {

    private IDCardUtils() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(IDCardUtils.class);

    /**
     * 中国公民身份证号码最小长度
     */
    public static final int MIN_LENGTH = 15;

    /**
     * 中国公民身份证号码最大长度
     */
    public static final int MAX_LENGTH = 18;

    /**
     * 最低年限
     */
    public static final int MIN_AGE = 1930;

    /**
     * 每位加权因子
     */
    private static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final int POWER_LENGTH = POWER.length;

    /**
     * 验证18位身份编码是否合法
     *
     * @param idCardNo 身份编码
     * @return 是否合法
     */
    public static boolean isLegalCardNo18(String idCardNo) {
        if (StringUtils.length(idCardNo) != MAX_LENGTH) {
            return false;
        }

        boolean bTrue = false;
        /* 前17位 */
        String code17 = idCardNo.substring(0, 17);
        /* 第18位 */
        String code18 = idCardNo.substring(17, MAX_LENGTH);
        if (StringUtils.isNumeric(code17)) {
            char[] cArr = code17.toCharArray();
            int[] iCard = convertCharToInt(cArr);
            int iSum17 = getPowerSum(iCard);
            /* 获取校验位 */
            String val = getCheckCode18(iSum17);
            if (val.length() > 0) {
                if (val.equalsIgnoreCase(code18)) {
                    bTrue = true;
                }
            }
        }
        return bTrue;
    }


    /**
     * 根据身份编号获取性别
     *
     * @param idCard 身份编号
     * @return 性别(1 - 男, 2 - 女)
     * @apiNote 若身份证格式错误则抛异常
     */
    public static String getGender(String idCard) {
        return getGenderOrDefault(idCard, "", false);
    }


    /**
     * 根据身份编号获取性别
     *
     * @param idCard       身份编号
     * @param defaultValue 默认值
     * @return 性别(1 - 男, 2 - 女) 默认值
     * @apiNote 若身份证错误则返回默认值
     */
    public static String getGenderOrDefault(String idCard, String defaultValue) {
        return getGenderOrDefault(idCard, defaultValue, true);
    }


    /**
     * 根据身份编号获取年龄
     *
     * @param idCard 身份编号
     * @return 年龄
     * @apiNote 若身份证格式错误则抛异常
     */
    public static int getAge(String idCard) {
        return getAgeOrDefault(idCard, 0, false);
    }


    /**
     * 根据身份编号获取年龄, 若不合法则返回默认值
     *
     * @param idCard 身份编号
     * @return 年龄
     * @readme: 若身份证错误则返回默认值
     */
    public static int getAgeOrDefault(String idCard, int defaultValue) {
        return getAgeOrDefault(idCard, defaultValue, true);
    }


    /**
     * 根据身份编号获取生日
     *
     * @param idCard 身份编号
     * @return 生日 格式: yyyy-MM-dd exp: 1984-01-01
     * @apiNote 若身份证格式错误则抛异常
     */
    public static String getBirth(String idCard) {
        return getBirthOrDefault(idCard, "", false);
    }


    /**
     * 根据身份编号获取生日
     *
     * @param idCard       身份编号
     * @param defaultValue 默认值
     * @return 格式: yyyy-MM-dd exp: 1984-01-01
     * @apiNote 若身份证错误则返回默认值
     */
    public static String getBirthOrDefault(String idCard, String defaultValue) {
        return getBirthOrDefault(idCard, defaultValue, true);
    }


    /**
     * 根据身份编号获取出生年
     *
     * @param idCard 身份编号
     * @return 出生年份 yyyy
     * @apiNote 若身份证格式错误则抛异常
     */
    public static int getYear(String idCard) {
        return getYearOrDefault(idCard, 0, false);
    }


    /**
     * 根据身份编号获取出生年
     *
     * @param idCard       身份证
     * @param defaultValue 默认值
     * @apiNote 若身份证错误则返回默认值
     */
    public static int getYearOrDefault(String idCard, int defaultValue) {
        return getYearOrDefault(idCard, defaultValue, true);
    }


    /**
     * 根据身份编号获取出生月份
     *
     * @param idCard 身份编号
     * @return 出生月份 MM
     * @apiNote 若身份证格式错误则抛异常
     */
    public static String getMonth(String idCard) {
        return getMonthOrDefault(idCard, "", false);
    }


    /**
     * 根据身份编号获取出生月份
     *
     * @param idCard       身份编号
     * @param defaultValue 默认值
     * @return 出生月份 MM
     * @apiNote 若身份证错误则返回默认值
     */
    public static String getMonthOrDefault(String idCard, String defaultValue) {
        return getMonthOrDefault(idCard, defaultValue, true);
    }


    /**
     * 根据身份编号获取出生天
     *
     * @param idCard 身份编号
     * @return 出生天 dd
     * @apiNote 若身份证格式错误则抛异常
     */
    public static String getDay(String idCard) {
        return getDayOrDefault(idCard, "", false);
    }


    /**
     * 根据身份编号获取出生天
     *
     * @param idCard       身份编号
     * @param defaultValue 默认值
     * @return 出生天 dd
     * @apiNote 若身份证错误则返回默认值
     */
    public static String getDayOrDefault(String idCard, String defaultValue) {
        return getDayOrDefault(idCard, defaultValue, true);
    }


    /**
     * 将字符数组转换成数字数组
     *
     * @param ca 字符数组
     * @return 数字数组
     */
    private static int[] convertCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
            }
        } catch (NumberFormatException e) {
            LOGGER.error("字符串转换数字异常", e);
        }
        return iArr;
    }


    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param iArr
     * @return 身份证编码。
     */
    private static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (POWER_LENGTH == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < POWER_LENGTH; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * POWER[j];
                    }
                }
            }
        }
        return iSum;
    }


    /**
     * 将power和值与11取模获得余数进行校验码判断
     *
     * @param iSum
     * @return 校验位
     */
    private static String getCheckCode18(int iSum) {
        String sCode;
        switch (iSum % 11) {
            case 10:
                sCode = "2";
                break;
            case 9:
                sCode = "3";
                break;
            case 8:
                sCode = "4";
                break;
            case 7:
                sCode = "5";
                break;
            case 6:
                sCode = "6";
                break;
            case 5:
                sCode = "7";
                break;
            case 4:
                sCode = "8";
                break;
            case 3:
                sCode = "9";
                break;
            case 2:
                sCode = "x";
                break;
            case 1:
                sCode = "0";
                break;
            case 0:
                sCode = "1";
                break;
            default:
                sCode = null;
                break;
        }
        return sCode;
    }


    private static String getGenderOrDefault(String idCard, String defaultValue, boolean isDefault) {
        if (isLegalCardNo18(idCard)) {
            return Integer.parseInt(idCard.substring(16, 17)) % 2 != 0 ? "1" : "2";
        } else if (isDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("身份证非法, idCard=" + idCard);
        }
    }


    public static int getAgeOrDefault(String idCard, int defaultValue, boolean isDefault) {
        if (isLegalCardNo18(idCard)) {
            return Year.now().getValue() - Integer.parseInt(idCard.substring(6, 10));
        } else if (isDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("身份证非法, idCard=" + idCard);
        }
    }


    private static String getBirthOrDefault(String idCard, String defaultValue, boolean isDefault) {
        if (isLegalCardNo18(idCard)) {
            return idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        } else if (isDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("身份证非法, idCard=" + idCard);
        }
    }


    private static int getYearOrDefault(String idCard, int defaultValue, boolean isDefault) {
        if (isLegalCardNo18(idCard)) {
            return Integer.parseInt(idCard.substring(6, 10));
        } else if (isDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("身份证非法, idCard=" + idCard);
        }
    }


    private static String getMonthOrDefault(String idCard, String defaultValue, boolean isDefault) {
        if (isLegalCardNo18(idCard)) {
            return idCard.substring(10, 12);
        } else if (isDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("身份证非法, idCard=" + idCard);
        }
    }


    private static String getDayOrDefault(String idCard, String defaultValue, boolean isDefault) {
        if (isLegalCardNo18(idCard)) {
            return idCard.substring(12, 14);
        } else if (isDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("身份证非法, idCard=" + idCard);
        }
    }
}