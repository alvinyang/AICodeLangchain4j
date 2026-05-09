package test;

import com.ai.code.Application;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest(classes = Application.class)
public class TestRewite {

    @Autowired
    private ChatClient chatClient;

    @Test
    public void testSearch() {
        Map<String, Object> map = chatClient.prompt("推荐一款机械键盘")
                .call().chatClientResponse().context();
        System.out.println(map);
    }
}
