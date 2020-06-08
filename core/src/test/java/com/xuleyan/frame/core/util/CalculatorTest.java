/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.util;

import com.xuleyan.frame.core.domain.Calculator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * 参数化测试
 *
 * @author xuleyan
 * @version CalculatorTest.java, v 0.1 2020-06-07 10:19 PM xuleyan
 */
@RunWith(Parameterized.class)
public class CalculatorTest {

    @Parameterized.Parameter(0)
    public String input;
    @Parameterized.Parameter(1)
    public int expected;
    private Calculator calc;

    @Parameterized.Parameters
    public static Collection<?> data() {
        return Arrays.asList(new Object[][]{
                {"1 + 2", 3},
                {"1+2+5", 8},
                {"123+456", 579},
                {"1+5+10", 16}
        });
    }

    @Before
    public void setUp() {
        calc = new Calculator();
    }

    @Test(timeout = 1000)
    public void testCalculate() {
        int r = calc.calculate(this.input);
        assertEquals(this.expected, r);
    }
}