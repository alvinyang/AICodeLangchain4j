package com.ai.code.controller;


import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Resource
    private ChatClient chatClient;

    @CrossOrigin("*") //前后端分离，跨域操作
    @RequestMapping(value = "/dashChat", produces = "text/stream;charset=utf-8")
    public Flux<String> dashChat(@RequestParam("question") String question){
        return chatClient.prompt().user(question).stream().content();
    }

}
