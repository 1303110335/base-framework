/**
 * xuleyan.com
 * Copyright (C) 2013-2021 All Rights Reserved.
 */
package com.xuleyan.frame.mask.filter;

import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 *
 * @author xuleyan
 * @version MaskValueFilter.java, v 0.1 2021-07-19 10:35 下午
 */
public class MaskValueFilter implements ValueFilter {


    private final Map<String, String[]> rules;

    public MaskValueFilter(Map<String, String[]> rules) {
        this.rules = rules;
    }

    @Override
    public Object process(Object object, String name, Object value) {
        if (value instanceof String && rules.containsKey(name)) {
            String[] values = rules.get(name);
            int left = Integer.parseInt(values[0]);
            int right = Integer.parseInt(values[1]);
            String valueStr = (String) value;

            // 保留左left,右right, 其他*
            int length = valueStr.length();
            return valueStr.substring(0, left) +
                    StringUtils.repeat("*", length - left - right) +
                    valueStr.substring(length - right);
        }
        return value;
    }
}