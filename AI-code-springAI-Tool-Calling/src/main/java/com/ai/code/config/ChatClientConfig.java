package com.ai.code.config;

import com.ai.code.service.CurrentTimeService;
import com.ai.code.service.ToolService;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.tool.support.ToolDefinitions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

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
//    @Bean
//    public ChatClient chatClient() {
//        return ChatClient.builder(dashScopeChatModel)
//                .defaultTools(toolService) // 注册tool注解
//                .build();
//    }

    @Bean
    public ChatClient chatClient() {
        Method method = ReflectionUtils.findMethod(CurrentTimeService.class,
                "getCurrentDateTime");
        // 构建MethodToolCallback
        // 通过Builder模式精细配置工具元数据
        MethodToolCallback toolCallback = MethodToolCallback.builder()
                // 配置工具定义
                .toolDefinition(ToolDefinitions.builder(method)
                        .name("get_current_time")
                        .description("获取当前的系统时间")
                        .build())
                // 指定要调用的方法
                .toolMethod(method)
                // 指定包含该方法的对象实例
                .toolObject(new CurrentTimeService())
                .build();
        // 注册到ChatClient
        return ChatClient.builder(dashScopeChatModel)
                .defaultToolCallbacks(toolCallback)
                .defaultTools(toolService) // 保留@Tool注解的方式
                .build();
    }
}
