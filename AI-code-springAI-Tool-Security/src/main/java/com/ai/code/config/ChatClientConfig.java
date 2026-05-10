package com.ai.code.config;

import com.ai.code.service.ToolService;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {
    private final DashScopeChatModel dashScopeChatModel;

    private final ToolService toolService;

    /**
     * 创建ChatClient并注册工具
     *
     * defaultTools() 方法说明：
     * - 自动扫描带有@Tool注解的public方法
     * - 根据方法签名自动生成ToolDefinition
     * - 注册到ChatClient的工具列表中供AI模型调用
     */
    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(dashScopeChatModel)
                .defaultTools(toolService) // 注册tool注解
                .build();
    }

}
