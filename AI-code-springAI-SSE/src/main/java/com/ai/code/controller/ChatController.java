package com.ai.code.controller;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Resource
    private DashScopeChatModel dashScopeChatModel;

    @CrossOrigin("*") //前后端分离，跨域操作
    @RequestMapping(value = "/dashChat", produces = "text/event-stream;charset=utf-8")
    public Flux<String> dashChat(@RequestParam("question") String question){
        return dashScopeChatModel.stream(question);
    }

}
