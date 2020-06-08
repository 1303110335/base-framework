/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import com.xuleyan.frame.core.domain.Tel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author xuleyan
 * @version TelUtil.java, v 0.1 2020-06-07 9:28 PM xuleyan
 */
public class TelUtil {

    private static Pattern p = compile("^(0\\d{2,3})-([1-9]\\d{5,7})$");

    public static Tel parse(String s) {
        Matcher m = p.matcher(s);
        if (m.matches()) {
            String s1 = m.group(1);
            String s2 = m.group(2);
            return new Tel(s1, s2);
        }

        return null;
    }
}