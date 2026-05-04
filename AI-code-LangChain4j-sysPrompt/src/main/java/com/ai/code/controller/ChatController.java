package com.ai.code.controller;

import com.ai.code.config.AIConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Date;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {
    private final AIConfig.Assistant assistant;

    @RequestMapping("/memory_chat/{question}")
    public String memoryChat(@PathVariable("question") String question) {
        return assistant.chat(question);
    }

    @RequestMapping(value = "/memory_stream/{question}", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryStream(@PathVariable("question") String question) {
        TokenStream tokenStream = assistant.stream(question, new Date().toString());
        return Flux.create(sink -> {
            tokenStream.onPartialResponse(sink::next).onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error).start();
        });
    }

}
