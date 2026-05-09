package com.ai.code.config;

import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class ChatTimeOutConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(
                        ClientHttpRequestFactories.get(
                                JdkClientHttpRequestFactory.class,
                                ClientHttpRequestFactorySettings.DEFAULTS
                                        .withConnectTimeout(Duration.ofMinutes(3)) //设定连接超时时间位3分钟
                                        .withReadTimeout(Duration.ofMinutes(3))    //设定获取结果超时时间为3分钟
                        )
                );
    }
}
