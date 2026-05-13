package com.ai.code.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class ChatConfig {
    @Resource
    private DashScopeChatModel dashScopeChatModel;

//    @Bean
//    public ChatClient chatClient(@Value("classpath:test.st") org.springframework.core.io.Resource resource){
//        PromptTemplate promptTemplate = PromptTemplate.builder()
//                //.template("你的名字叫{name}，是以为{zhiye}工程师")
//                .resource(resource) // 通过读取外部文件加装资源数据
//                .build();
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("zhiye", "java");
//        map.put("name", "张三");
//        return ChatClient.builder(dashScopeChatModel)
//                //.defaultSystem("你的名字叫小白，是以为Java工程师")
//                .defaultSystem(promptTemplate.render(map)) // 使用模板提示词
//                .build();
//    }

    @Bean(value = "memoryChatClient")
    public ChatClient chatClient(ChatMemory chatMemory){
        return ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
