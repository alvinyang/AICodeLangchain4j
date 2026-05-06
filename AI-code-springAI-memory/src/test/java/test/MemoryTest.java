package test;

import com.ai.code.Application;
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

    /**
     * 阿里百炼平台访问人数多时，可能存在超时现象，当前官方没有提供解决方案，可以手动设置连接的超时时间
     * 这里可以设置到配置类中的公共方法，当前临时规避
     * @return
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(
                        ClientHttpRequestFactories.get(
                                JdkClientHttpRequestFactory.class,
                                ClientHttpRequestFactorySettings.DEFAULTS
                                        .withConnectTimeout(Duration.ofMinutes(3)) //设定连接超时时间位3分钟
                                        .withReadTimeout(Duration.ofMinutes(3))    //设定获取结果超时时间为3分钟
                        )
                );
    }

    @Test
    public void testMemory(){
        ChatMemory memory = MessageWindowChatMemory.builder().build(); // 通过chatMemory存储会话信息
        String uuid = UUID.randomUUID().toString();
        memory.add(uuid, new UserMessage("我是一名大龄程序员"));

        String msg = dashScopeChatModel.call(Prompt.builder().messages(memory.get(uuid)).build())
                .getResult().getOutput().getText();

        memory.add(uuid, new AssistantMessage(msg));// 记录大模型返回的内容

        System.out.println("第一次会话：" + msg);
        System.out.println("==========================================");

        memory.add(uuid, new UserMessage("我是谁"));

        String msg02 = dashScopeChatModel.call(Prompt.builder().messages(memory.get(uuid)).build())
                .getResult().getOutput().getText();

        System.out.println("第二次会话：" + msg02);
    }
}
