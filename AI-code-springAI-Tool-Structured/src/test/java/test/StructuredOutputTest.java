package test;

import com.ai.code.Application;
import com.ai.code.common.Product;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class StructuredOutputTest {

    @Autowired
    private DashScopeChatModel dashScopeChatModel;

    @Autowired
    private BeanOutputConverter<Product> productOutputConverter;

    @Test
    public void testStructured(){

        // 使用BeanOutputConverter自动生成格式指令（推荐）
        // getFormat()方法返回根据Product结构自动生成的JSON Schema指令
        String format = productOutputConverter.getFormat();
        String prompt = """
                请根据用户需求推荐一款合适的商品。
                用户需求：需要一款适合办公使用的电子设备
                
                请按照以下JSON格式返回结果：
                %s
                """.formatted(format);

        // 调用大模型
        ChatResponse chatResponse = dashScopeChatModel.call(new Prompt(prompt));
        String jsonContent = chatResponse.getResult().getOutput().getText();
        System.out.println("返回JSON：" + jsonContent);

        // 使用BeanOutputConverter将JSON转换为Product对象
        // 内部使用Jackson进行JSON反序列化
        Product product = productOutputConverter.convert(jsonContent);
        // 输出转换后的对象
        System.out.println("转换后的Product对象：" + product);
    }

    @Test
    public void testChatClientOutput() {
        // 创建ChatClient
        ChatClient chatClient = ChatClient.builder(dashScopeChatModel).build();

        // 直接指定输出类型为Product
        // Spring AI会自动处理格式指令和转换
        Product product = chatClient.prompt()
                .user(u -> u.text("推荐一款适合游戏的电脑"))
                .call()
                .entity(Product.class);  // 指定输出类型

        System.out.println("商品名称：" + product.name());
        System.out.println("商品价格：" + product.price());
    }
}
