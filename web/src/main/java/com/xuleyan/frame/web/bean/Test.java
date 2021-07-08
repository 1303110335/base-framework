/**
 * bianque.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.web.bean;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 *
 * @author xuleyan
 * @version Test.java, v 0.1 2020-07-29 4:25 下午
 */
public class Test {

    public String testMethod(User user) {
        return "haha";
    }

    public String testNoParamMethod() {
        return "noMethod";
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Test test = new Test();

        User user = new User();
        user.setName("haha");
        String json = JSON.toJSONString(user);
        test.invokeMethodByObj("testmethod", json);
    }

    /**
     * 根据对象和方法名称来调用方法
     * @param methodInput
     * @param jsonParam
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void invokeMethodByObj(String methodInput, String jsonParam) throws InvocationTargetException, IllegalAccessException {
        // 有参数的方法
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (methodInput.equalsIgnoreCase(method.getName())) {
                Parameter[] parameters = method.getParameters();
                Parameter parameter = parameters[0];
                Class<?> type = parameter.getType();
                Object param = JSON.parseObject(jsonParam, type);
                Object result = method.invoke(this, param);
                System.out.println(result);
            }
        }
    }


}