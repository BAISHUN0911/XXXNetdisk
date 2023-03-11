package com.lanqiao.netdisk.util;

import org.springframework.core.env.Environment;

/**
 * @description: 通过该类的 getProperty 方法，可以获取 application.properties 中的配置
 * @author: BAISHUN
 * @date: 2023/3/8
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
public class PropertiesUtil {

    private static Environment env = null;

    public static void setEnvironment(Environment env){
        PropertiesUtil.env = env;
    }

    public static String getProperty(String key){
        return PropertiesUtil.env.getProperty(key);
    }

}
