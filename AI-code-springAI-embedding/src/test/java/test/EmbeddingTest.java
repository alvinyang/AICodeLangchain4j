package test;

import com.ai.code.Application;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = Application.class)
public class EmbeddingTest {

    @Autowired
    private DashScopeEmbeddingModel dashScopeEmbeddingModel;

    @Test
    public void testEmbedding() {
        String text = "你好";
        EmbeddingResponse embeddingResponse = dashScopeEmbeddingModel.embedForResponse(List.of(text));
        float[] output = embeddingResponse.getResult().getOutput();
        System.out.println(Arrays.toString(output));

        float[] embed = dashScopeEmbeddingModel.embed(text);
        System.out.println("单个文本向量化：" + Arrays.toString(embed));
    }
}
