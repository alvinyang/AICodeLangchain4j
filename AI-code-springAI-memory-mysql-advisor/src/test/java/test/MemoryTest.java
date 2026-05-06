package test;

import com.ai.code.Application;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = Application.class)
public class MemoryTest {

    @Autowired
    @Qualifier("memoryChatClient")
    private ChatClient chatClient;

    @Test
    public void testMemory() {
        String uuid = "UserId_converSationId";
        System.out.println(chatClient.prompt().
                advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,uuid))
                .user("我是一名30岁的厨师")
                .call()
                .content());
        System.out.println(chatClient.prompt().
                advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID,uuid))
                .user("我的职业是什么")
                .call()
                .content());
    }
}
