package com.ai.code.config;

import com.alibaba.cloud.ai.memory.redis.JedisRedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RedisChatMemoryConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

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

    @Bean
    public ChatMemory redisChatMemory(){
        JedisRedisChatMemoryRepository redisChatMemory = JedisRedisChatMemoryRepository.builder()
                .host(host)
                .port(port)
                .build();
        return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(redisChatMemory).build();
    }
}
