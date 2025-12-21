package com.ruoyi.wms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Dify API配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "dify")
public class DifyConfig {

    /**
     * Dify API地址
     */
    private String apiUrl;

    /**
     * Dify API密钥
     */
    private String apiKey;

    /**
     * Dify应用ID
     */
    private String appId;

}
