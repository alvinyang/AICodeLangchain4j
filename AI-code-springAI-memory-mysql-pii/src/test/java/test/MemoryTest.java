package test;

import com.ai.code.Application;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = Application.class)
public class MemoryTest {

    @Autowired
    @Qualifier("memoryChatClient")
    private ChatClient chatClient;

    @Autowired
    @Qualifier("digestClient")
    private ChatClient digestClient;

    @Autowired
    private ChatMemory chatMemory;

    @Test
    public void testMemory() {
        String uuid = "UserId_converSationId";
        System.out.println(chatClient.prompt().
                advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, uuid))
                .user("我是一名30岁的厨师")
                .call()
                .content());
        System.out.println(chatClient.prompt().
                advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, uuid))
                .user("我的职业是什么")
                .call()
                .content());
    }

    @Test
    public void testForget() {
        String uuid = "UserId_converSationId";
        List<Message> messages = chatMemory.get(uuid);
        if (messages.size() >= 5) {
            // 大于5条会话时，进行摘要汇总，并把之前的记录删掉，添加摘要信息
            AssistantMessage assistantMessage= digestClient.prompt()
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, uuid))
                    .call().chatResponse().getResult().getOutput();
            chatMemory.clear(uuid);
            chatMemory.add(uuid, assistantMessage);
        }
        System.out.println(chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, uuid))
                .user("我的住址是哪里")
                .call()
                .content());
    }

    @Test
    public void testPii() {
        String uuid = "UserId_converSationId";
        System.out.println(chatClient.prompt().
                advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, uuid))
                .user("我的前面提到的住址和手机号信息是什么")
                .call()
                .content());
    }
}
