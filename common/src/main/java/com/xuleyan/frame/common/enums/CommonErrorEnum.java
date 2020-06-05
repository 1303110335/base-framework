package com.xuleyan.frame.common.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * @author liujing01
 * @version CommonErrorEnum.java, v 0.1 2018-11-08 10:04
 */
public enum CommonErrorEnum {
    /**
     * 错误码枚举
     */
    SYS_ERROR("网络繁忙，请稍后再试","9999"),
    NOT_SUPPORT_METHOD("不支持该HTTP METHOD","9998"),
    API_NOT_EXIST("接口不存在","9997"),
    INVALID_PARAM("接口参数不正确","9996"),
    PERMISSION_DENIED("没有权限","9995"),
    PARAM_VALIDATE_ERROR( "参数验证失败","9994"),
    SIGN_ERROR("签名验证失败","9993"),
    NO_FIND_CURRENT_RUNTIME_ENV("未找到当前运行环境","9992"),
    LOGIN_TIMEOUT("登陆超时", "9991");

    private String name;
    private String value;

    CommonErrorEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static CommonErrorEnum getByValue(String value) {
        CommonErrorEnum[] valueList = CommonErrorEnum.values();
        for (CommonErrorEnum v : valueList) {
            if (StringUtils.equalsIgnoreCase(v.getValue(), value)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for property <tt>value</tt>.
     *
     * @return property value of value
     */
    public String getValue() {
        return value;
    }
}