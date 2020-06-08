/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author xuleyan
 * @version RegixUtil.java, v 0.1 2020-06-07 6:42 PM xuleyan
 */
public class RegixUtil {

    /**
     * 年份的正则表达式
     * @param year
     * @return
     */
    public static boolean checkYear(String year) {
        if (year == null) {
            return false;
        }
        return year.matches("19\\d\\d");
    }

    /**
     * 电话号码（区号-电话号码）
     * @param s
     * @return
     */
    public static boolean isValidTel(String s) {
        return s.matches("^0\\d{2,3}-[1-9]\\d{5,7}$");
    }

    /**
     * 判断有几个0
     * @param s
     * @return
     */
    public static int zeros(String s) {
        Pattern p = compile("^\\d+?(0*)$");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            String zeroStr = m.group(1);
            return zeroStr.length();
        }
        throw new IllegalArgumentException("Not a number");
    }



}