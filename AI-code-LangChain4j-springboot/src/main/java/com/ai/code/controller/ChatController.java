package com.ai.code.controller;

import com.ai.code.config.AIConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class ChatController {
    private final QwenChatModel qwenChatModel;

    private final QwenStreamingChatModel qwenStreamingChatModel;

    private final AIConfig.Assistant assistant;

    @RequestMapping("/chat")
    public String chat(@RequestParam("question") String question) {
        System.out.println("当前的问题：" + question);
        return qwenChatModel.chat(question);
    }
    @RequestMapping(value = "/stream", produces = "text/stream;charset=UTF-8")
    public Flux<String> stream(@RequestParam("question") String question) {
        // 流式响应的内容需要使用Flux对象进行返回
        // 这里创建一个Flux对象
        Flux<String> flux = Flux.create(stringFlux ->{
            qwenStreamingChatModel.chat(question, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    stringFlux.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    stringFlux.complete();
                }

                @Override
                public void onError(Throwable error) {
                    System.out.println(error.getMessage());
                    stringFlux.error(error);
                }
            });
        });
        // 直接将Flux对象进行返回，与前端建立长连接
        return flux;
    }

    @RequestMapping("/memory_chat")
    public String memoryChat(@RequestParam(value = "question", defaultValue = "我叫什么") String question) {
        return assistant.chat(question);
    }

    @RequestMapping(value = "/memory_stream", produces = "text/stream;charset=UTF-8")
    public Flux<String> memoryStream(@RequestParam(value = "question", defaultValue = "我叫什么") String question) {
        TokenStream tokenStream = assistant.stream(question);
        return Flux.create(sink -> {
            tokenStream.onPartialResponse(sink::next).onCompleteResponse(chatResponse -> sink.complete())
                    .onError(sink::error).start();
        });
    }

}
