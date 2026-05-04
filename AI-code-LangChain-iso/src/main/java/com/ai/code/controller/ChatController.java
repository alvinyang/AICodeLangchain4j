package com.ai.code.controller;

import com.ai.code.config.AIConfig;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 会话隔离，添加messageID
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {
    private final AIConfig.Assistant assistant;

    @RequestMapping("/memory_chat/{question}/{mid}")
    public String memoryChat(@PathVariable("question") String question, @PathVariable("mid") Integer mid) {
        return assistant.chat(mid, question);
    }

    @RequestMapping(value = "/memory_stream/{question}/{mid}", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryStream(@PathVariable("question") String question, @PathVariable("mid") Integer mid) {
        TokenStream tokenStream = assistant.stream(mid, question);
        return Flux.create(sink -> {
            tokenStream.onPartialResponse(sink::next).onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error).start();
        });
    }

}
