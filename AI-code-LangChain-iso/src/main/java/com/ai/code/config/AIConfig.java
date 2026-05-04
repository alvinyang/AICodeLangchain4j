package com.ai.code.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {
    public interface Assistant {
        String chat(@MemoryId Integer mid, @UserMessage String message);
        TokenStream stream(@MemoryId Integer mid, @UserMessage String message);
    }
    @Bean
    public Assistant assistant(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        return AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                //.chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder().maxMessages(10).id(memoryId).build()) // 该方式适合少量记忆的数据存储
                .chatMemoryProvider(memoryId -> new HashMapChatMemory(memoryId.toString(),10)) // 类似Redis方式存储较多的历史记录的方式，这里使用hashMap方式
                .build();
    }
}
