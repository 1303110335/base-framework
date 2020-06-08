package com.xuleyan.frame.core.util;


import com.xuleyan.frame.core.domain.Tel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
@Slf4j
public class RegixUtilTest {

    @Test
    public void checkYear() {
        assertTrue(RegixUtil.checkYear("1999"));
        assertFalse(RegixUtil.checkYear(null));
        assertFalse(RegixUtil.checkYear(""));
        assertFalse(RegixUtil.checkYear("19"));
        assertFalse(RegixUtil.checkYear("190A"));
        assertFalse(RegixUtil.checkYear("19001"));
        assertFalse(RegixUtil.checkYear("1900s"));
        assertFalse(RegixUtil.checkYear("2900"));
        assertFalse(RegixUtil.checkYear("A900"));
    }

    @Test
    public void checkTel() {
        assertTrue(RegixUtil.isValidTel("010-6785467"));
        assertFalse(RegixUtil.isValidTel("0101-678546700"));
        assertFalse(RegixUtil.isValidTel("010#-6785467"));
        assertFalse(RegixUtil.isValidTel("010-6785467s"));
        assertFalse(RegixUtil.isValidTel("110-6785467"));

        assertTrue(RegixUtil.isValidTel("010-1678546"));
        assertFalse(RegixUtil.isValidTel("010-0678546"));
    }

    @Test
    public void testIsValidTel() {
        assertEquals(new Tel("010", "123456"), TelUtil.parse("010-123456"));
        assertEquals(new Tel("0123", "12345678"), TelUtil.parse("0123-12345678"));

        assertNull(TelUtil.parse("123-12345678"));
        assertNull(TelUtil.parse("010-01234567"));
        assertNull(TelUtil.parse("010#12345678"));

    }

    @Test
    public void testZeros() {
        assertEquals(0, RegixUtil.zeros("123456"));
        assertEquals(1, RegixUtil.zeros("123450"));
        assertEquals(2, RegixUtil.zeros("123400"));
        assertEquals(3, RegixUtil.zeros("123000"));
        assertEquals(4, RegixUtil.zeros("120000"));
        assertEquals(2, RegixUtil.zeros("100100"));

    }

    /**
     * 字符串分割
     * 结果：
     * [a, b, c]
     * [a, , b, c]
     * [a, b, c]
     * [a, b, c]
     */
    @Test
    public void testSplit() {
        String[] split = "a b c".split("\\s");
        System.out.println(Arrays.asList(split));

        String[] split2 = "a  b c".split("\\s");
        System.out.println(Arrays.asList(split2));

        String[] split3 = "a  b c".split("\\s+");
        System.out.println(Arrays.asList(split3));

        String[] split4 = "a , b ; c".split("[,;\\s]+");
        System.out.println(Arrays.asList(split3));
    }

    @Test
    public void testFind() {
        String s = "The quick brown fox jumps over the lazy dog.";
        Pattern p = Pattern.compile("\\w*o\\w*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(s);
        while (matcher.find()) {
            String substring = s.substring(matcher.start(), matcher.end());
            System.out.println(substring + ", start = " + matcher.start() + ", end = " + matcher.end());
        }
    }

    @Test
    public void testReplaceAll() {
        String s = "The    quick brown  fox jumps over the lazy dog.";
        String r = s.replaceAll("\\s+", " ");
        System.out.println(r);
        String r2 = r.replaceAll("(\\w+)", "<b>$1</b>");
        System.out.println(r2);

    }
}
