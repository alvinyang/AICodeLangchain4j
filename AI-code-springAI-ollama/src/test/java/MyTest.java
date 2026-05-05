import com.ai.code.StartApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = StartApp.class)
public class MyTest {

    @Resource
    private OllamaChatModel ollamaChatModel;

    @Test
    public void test(){
        String msg = ollamaChatModel.call("你好，你是什么模型");
        System.out.println(msg);
    }
}
