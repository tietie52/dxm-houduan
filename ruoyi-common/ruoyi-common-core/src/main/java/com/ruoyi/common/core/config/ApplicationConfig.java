package com.ruoyi.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * 程序注解配置
 *
 * @author Lion Li
 */
@AutoConfiguration
@EnableAspectJAutoProxy
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
public class ApplicationConfig {

    /**
     * 配置RestTemplate用于HTTP请求
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        return new RestTemplate(factory);
    }

}
