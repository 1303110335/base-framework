/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author xuleyan
 * @version JunitTest.java, v 0.1 2020-06-07 10:17 PM xuleyan
 */
public class JunitTest {

    /**
     * 初始化耗时资源
     */
    @BeforeClass
    public static void beforeClass() {
        System.out.println("before class");
    }

    /**
     * 用于清理beforeClass创建的资源
     */
    @AfterClass
    public static void afterClass() {
        System.out.println("after class");
    }

    /**
     * 初始化测试对象
     */
    @Before
    public void setUp() {
        System.out.println("before");
    }

    @Test
    public void testReplaceAll() {
        String s = "The    quick brown  fox jumps over the lazy dog.";
        String r = s.replaceAll("\\s+", " ");
        System.out.println(r);
    }

    /**
     * 用于清理before创建的对象
     */
    @After
    public void tear() {
        System.out.println("after");
    }
}