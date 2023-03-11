package com.lanqiao.netdisk.config;

import com.lanqiao.netdisk.util.PropertiesUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @description: 用于读取自定义配置信息，如
 * @author: BAISHUN
 * @date: 2023/3/8
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Configuration
public class PropertiesConfig {

    @Resource
    private Environment env;

    @PostConstruct
    public void setProperties(){
        PropertiesUtil.setEnvironment(env);
    }

}
