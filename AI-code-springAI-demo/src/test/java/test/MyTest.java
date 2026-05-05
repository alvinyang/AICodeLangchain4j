package test;

import com.ai.code.StartApp;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StartApp.class)
public class MyTest {

    @Resource
    private DashScopeChatModel dashScopeChatModel;

    @Test
    public void test(){
        String msg = dashScopeChatModel.call("你好，你是什么模型");
        System.out.println(msg);
    }
}
