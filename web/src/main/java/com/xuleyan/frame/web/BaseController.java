/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.xuleyan.frame.web;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.xuleyan.frame.web.domain.ApiResponse;

/**
 * @author wujn
 * @version BaseController.java, v 0.1 2018-08-28 20:04
 */
public class BaseController {
    private static final SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();

    protected ApiResponse result(boolean success, Object data, String errorCode, String errorMsg) {
        ApiResponse result = new ApiResponse();
        if (success) {
            result.setData(data);
            result.setSuccess(true);
            result.setErrorMsg(null);
        } else {
            result.setData(null);
            result.setSuccess(false);
            result.setErrorCode(errorCode);
            result.setErrorMsg(errorMsg);
        }
        return result;
    }

    protected ApiResponse success(Object data) {
        return result(true, data, "0", null);
    }

    protected ApiResponse error(String errorCode, String errorMsg) {
        return result(false, null, errorCode, errorMsg);
    }

    protected ApiResponse success() {
        return result(true, null, "0", null);
    }
}
