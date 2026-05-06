package test;

import com.ai.code.Application;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = Application.class)
public class MemoryTest {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    @Autowired
    private ChatMemory jdbcChatMemory;

    @Test
    public void testJdbcMemory(){
        String uuid = UUID.randomUUID().toString();
        jdbcChatMemory.add(uuid, new UserMessage("我是小明"));

        String msg = dashScopeChatModel.call(Prompt.builder().messages(jdbcChatMemory.get(uuid)).build())
                .getResult().getOutput().getText();

        jdbcChatMemory.add(uuid, new AssistantMessage(msg));// 记录大模型返回的内容

        System.out.println("第一次会话：" + msg);
        System.out.println("==========================================");

        jdbcChatMemory.add(uuid, new UserMessage("我是谁"));

        String msg02 = dashScopeChatModel.call(Prompt.builder().messages(jdbcChatMemory.get(uuid)).build())
                .getResult().getOutput().getText();
        jdbcChatMemory.add(uuid, new AssistantMessage(msg02));// 记录大模型返回的内容
        System.out.println("第二次会话：" + msg02);
    }
}
