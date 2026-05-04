package com.ai.code.config;

import com.ai.code.service.TestService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AIConfig {
    private final TestService testService;
    public interface Assistant {
        String chat(String message);

        @SystemMessage("""
                你是一名名为小欧的资深Java工程师，你能通过用户的问题给出专业的技术指导。
                当前日期:{{cur_time}}
                """)
        TokenStream stream(@UserMessage String message, @V("cur_time") String time);
    }
    @Bean
    public Assistant assistant(ChatModel chatModel, StreamingChatModel streamingChatModel) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
        Assistant assistant = AiServices.builder(Assistant.class)
                .tools(testService)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .chatMemory(chatMemory)
                .build();
        return assistant;
    }
}
