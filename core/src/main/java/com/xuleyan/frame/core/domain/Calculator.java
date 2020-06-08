/**
 * fshows.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.core.domain;

/**
 * @author xuleyan
 * @version Calculator.java, v 0.1 2020-06-07 10:23 PM xuleyan
 */
public class Calculator {
    public int calculate(String input) {
        int sum = 0;
        String[] integers = input.split("\\+");
        if (integers.length > 0) {
            for (String integer : integers) {
                int i = Integer.parseInt(integer.trim());
                sum += i;
            }
        }
        return sum;
    }
}