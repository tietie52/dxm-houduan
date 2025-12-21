package com.ruoyi.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import lombok.extern.slf4j.Slf4j;

/**
 * 程序注解配置
 *
 * @author Lion Li
 */
@Slf4j
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
        // 配置SSL支持
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            }, new java.security.SecureRandom());
            
            // 创建SimpleClientHttpRequestFactory并设置SSL上下文
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    super.prepareConnection(connection, httpMethod);
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                        ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                    }
                }
            };
            
            // 设置超时时间
            factory.setConnectTimeout(120000); // 连接超时2分钟
            factory.setReadTimeout(120000);    // 读取超时2分钟
            factory.setBufferRequestBody(true);
            
            // 创建RestTemplate实例
            return new RestTemplate(factory);
        } catch (Exception e) {
            log.error("SSL配置异常: {}", e.getMessage(), e);
            return new RestTemplate(new SimpleClientHttpRequestFactory());
        }
    }

}
