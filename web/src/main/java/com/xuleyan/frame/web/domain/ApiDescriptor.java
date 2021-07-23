/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.web.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuleyan
 * @version ApiDescriptor.java, v 0.1 2018-06-08 17:52 xuleyan
 */
public class ApiDescriptor {
    /**
     * 方法签名拆分正则
     */
    private static final Pattern PATTERN = Pattern.compile("\\s+(.*)\\s+((.*)\\.(.*))\\((.*)\\)", Pattern.DOTALL);
    /**
     * 默认方法组成元素的数量
     */
    private static final int DEFAULT_METHOD_ELEMENT_COUNT = 6;
    /**
     * 对应 api 中的方法名
     */
    private String methodName;
    /**
     * 简单方法名
     */
    private String simpleName;
    /**
     * 方法全限定名
     */
    private String methodFullName;
    /**
     * 方法对象
     */
    private Method method;
    /**
     * 类全限定名
     */
    private String classFullName;
    /**
     * 返回值全限定名
     */
    private String returnFullName;
    /**
     * 参数全限定名
     */
    private String[] paramFullNameList;
    /**
     * spring bean 名称
     */
    private String beanName;

    public ApiDescriptor(String methodFullame, String apiMethod, String beanName) throws ClassNotFoundException {
        List<String> items = splitMethodName(methodFullame);
        if (items.size() == DEFAULT_METHOD_ELEMENT_COUNT) {
            this.methodName = apiMethod;
            this.returnFullName = items.get(1);
            this.methodFullName = items.get(2);
            this.classFullName = items.get(3);
            this.simpleName = items.get(4);
            String paramItemStr = items.get(5);
            if (!StringUtils.isBlank(paramItemStr)) {
                this.paramFullNameList = new String[]{items.get(5)};
            } else {
                this.paramFullNameList = new String[]{};
            }
            this.beanName = beanName;
            this.method = ReflectionUtils.findMethod(Class.forName(classFullName),
                    simpleName,
                    Class.forName(paramFullNameList[0]));
        }
    }

    /**
     * 通过方法签名拆分各个元素名称
     *
     * @param content 方法全称
     * @return
     */
    private static List<String> splitMethodName(String content) {
        ArrayList<String> result = new ArrayList<>();
        if (StringUtils.isBlank(content)) {
            return result ;
        }

        final Matcher matcher = PATTERN.matcher(content);
        if (matcher.find()) {
            int groupCount = matcher.groupCount() + 1;
            for (int i = 0; i < groupCount; i++) {
                result.add(matcher.group(i));
            }
        }
        return result;
    }

    /**
     * Getter method for property <tt>methodName</tt>.
     *
     * @return property value of methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Getter method for property <tt>simpleName</tt>.
     *
     * @return property value of simpleName
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * Getter method for property <tt>methodFullName</tt>.
     *
     * @return property value of methodFullName
     */
    public String getMethodFullName() {
        return methodFullName;
    }

    /**
     * Getter method for property <tt>method</tt>.
     *
     * @return property value of method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Getter method for property <tt>classFullName</tt>.
     *
     * @return property value of classFullName
     */
    public String getClassFullName() {
        return classFullName;
    }

    /**
     * Getter method for property <tt>returnFullName</tt>.
     *
     * @return property value of returnFullName
     */
    public String getReturnFullName() {
        return returnFullName;
    }

    /**
     * Getter method for property <tt>paramFullNameList</tt>.
     *
     * @return property value of paramFullNameList
     */
    public String[] getParamFullNameList() {
        return paramFullNameList;
    }

    /**
     * Getter method for property <tt>beanName</tt>.
     *
     * @return property value of beanName
     */
    public String getBeanName() {
        return beanName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}