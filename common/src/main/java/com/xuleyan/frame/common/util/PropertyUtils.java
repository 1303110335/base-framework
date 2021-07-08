/**
 * bianque.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.xuleyan.frame.common.util;

import cn.hutool.core.io.resource.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author xuleyan
 * @version PropertyUtils.java, v 0.1 2020-08-02 11:55 上午
 */
public class PropertyUtils {

    /**
     * 解析文件
     * @param filePath
     * @return
     * @throws IOException
     */
    public static Properties parseProperties(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        Properties properties = new Properties();
        properties.load(resource.getStream());
        return properties;
    }
}