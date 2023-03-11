package com.lanqiao.netdisk.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: Druid数据源配置类
 * @author: BAISHUN
 * @date: 2023/3/8
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DruidDataSource druidDataSource(){
        return new DruidDataSource();
    }


}
