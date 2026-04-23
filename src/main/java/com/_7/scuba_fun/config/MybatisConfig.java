package com._7.scuba_fun.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    /**
     * 透過 Java Config 全域開啟駝峰命名自動轉換 (map-underscore-to-camel-case)
     * 完美避開 Spring Boot 3.4.x 與 MyBatis Starter 4.0.1 在 YAML 解析時的 NPE Bug。
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }
}
