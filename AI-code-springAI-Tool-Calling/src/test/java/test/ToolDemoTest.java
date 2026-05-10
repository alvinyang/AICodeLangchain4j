package test;

import com.ai.code.Application;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class ToolDemoTest {

    @Autowired
    private ChatClient chatClient;

    @Test
    public void test01(){
        String question = "查询南京的添加";

        String content = chatClient.prompt()
                .user(question)
                .call()
                .content();

        System.out.println("注解方式调用工具结果：\n" + content);
    }

    @Test
    public void test02() {
        String question = "查询当前时间";
        String content = chatClient.prompt().user(question).call().content();
        System.out.println("MethodToolCallback 方法调用工具结果：\n" + content);
    }
}
