package com.xuleyan.frame.core.constants;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-04-20
 **/
public class AcsCoreConstants {



    /**
     * 登入
     */
    public static final String SSO_LOGIN = "/login";
    /**
     * 登出
     */
    public static final String SSO_LOGOUT = "/logout";


    public static final String AND = "&&";

    public static final String ORG_GROUP_PARAMS = "orgGroup=";

    public static final String DEFAULT_ENCRYPT_KEY = "ALJKBQSJBQCOMMON";
    public static final String ORG_GROUP_KEY = "org";


    /**
     * SSO服务中心URL
     */
    public static String ACS_SERVER_URL = "AcsServerUrl";

    /**
     * cookie中的BQSESSIONID
     */
    public static final String SESSION_ID = "BQSESSIONID";

    /**
     * cookie中的BQSESSIONID
     */
    public static final String BQ_TOKEN = "BQTOKEN";

    /**
     * cookie中的BQSESSIONID
     */
    public static final String BQ_USER_ID = "BQUSERID";

    public static final String BQ_APP_ID = "BQAPPID";

    /**
     * 应用系统编码
     */
    public static final String APP_ID = "APPID";

    /**
     * 应用系统码value
     */
    public static String APP_ID_VALUE = "AppIdValue";

    /**
     * SSO 服务中心登出
     */
    public static String LOGOUT_URI = "logoutUri";

    /**
     * 登录
     */
    public static String LOGIN_URI = "loginUri";

    /**
     *
     */
    public static final String REDIRECT_URL = "redirectUrl";

    /**
     * 跳转地址参数
     */
    public static final String REDIRECT_PARAMS = "?" + REDIRECT_URL + "=";

    /**
     * 跳转设置cookie
     */
    public static final String SET_SESSION_ID = "?" + SESSION_ID + "=";


    /**
     * 用户只能一个终端登录,存入redis前缀
     */
    public static final String USER_INFO_PREFIX = "u";

    /**
     * redis数据过期时间,默认30分钟,单位秒
     */
    public static Integer EXPIRE_TIME_VALUE = 28800;

    /**12小时*/
    public static Integer LOGIN_EXPIRE_TIME_VALUE = 43200;
    /**
     * 请求类型
     */
    public static String REQUEST_TYPE = "";
    /**
     * 机构组
     */
    public static String ORG_GROUP = "ORGGROUP";

    /**
     * 三方接入时cookie中的BQSESSIONID
     */
    public static final String THIRD_COOKIE_ID = "BQSESSIONTOKEN";

    /**
     * 默认加密key
     */
    public static final String DEFAULT_ENCRYPT_KEY_LOGIN = "b1q2h3e4a5lthacs";

    /**
     * 错误数据标识
     */
    public static final String FAIL_DATA_KEY = "failData";

    /**
     * 成功数据标识
     */
    public static final String SUCCESS_DATA_KEY = "successData";
}
