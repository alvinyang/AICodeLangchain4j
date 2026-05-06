package test;

import com.ai.code.Application;
import com.ai.code.config.RedisChatMemoryConfig;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.UUID;

@SpringBootTest(classes = Application.class)
public class MemoryTest {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    @Autowired
    private ChatMemory redisChatMemory;

    @Test
    public void testMemory(){
        String uuid = UUID.randomUUID().toString();
        redisChatMemory.add(uuid, new UserMessage("我是张三"));

        String msg = dashScopeChatModel.call(Prompt.builder().messages(redisChatMemory.get(uuid)).build())
                .getResult().getOutput().getText();

        redisChatMemory.add(uuid, new AssistantMessage(msg));// 记录大模型返回的内容

        System.out.println("第一次会话：" + msg);
        System.out.println("==========================================");

        redisChatMemory.add(uuid, new UserMessage("我是谁"));

        String msg02 = dashScopeChatModel.call(Prompt.builder().messages(redisChatMemory.get(uuid)).build())
                .getResult().getOutput().getText();
        redisChatMemory.add(uuid, new AssistantMessage(msg02));// 记录大模型返回的内容
        System.out.println("第二次会话：" + msg02);
    }
}
