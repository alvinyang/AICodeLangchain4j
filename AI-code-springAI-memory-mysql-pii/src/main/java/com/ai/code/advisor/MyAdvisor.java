package com.ai.code.advisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MyAdvisor implements BaseAdvisor {

    @Autowired
    @Qualifier(value = "piiChatClient")
    private ChatClient piiChatClient;

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        String text = chatClientRequest.prompt().getUserMessage().getText();
        String content = piiChatClient.prompt().user("请对以下文本进行脱敏：\n" + text)
                .call().content();// 脱敏后的数据
        System.out.println("脱敏后的内容：" + content);
        // 重新构建脱敏的请求信息
        return chatClientRequest.mutate()
                .prompt(Prompt.builder().content(content).build())
                .build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
