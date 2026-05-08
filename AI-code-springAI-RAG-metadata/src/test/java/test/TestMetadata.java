package test;

import com.ai.code.Application;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = Application.class)
public class TestMetadata {

    @Autowired
    private VectorStore milvusVectorStore;

    @Test
    public void testMetadata() {
        List<String> batchTexts = new ArrayList<>();
        batchTexts.add("【2025新款】智能保温杯 316不锈钢 温度显示 24小时保温 白色 500ml");
        batchTexts.add("无线蓝牙耳机 半入耳式 降噪高清通话 续航40小时 适配安卓/苹果");
        batchTexts.add("机械键盘 青轴 104键 背光有线 电竞游戏专用 全键无冲 黑色");
        // 将documents设置元数据后写到向量数据库中
        List<Document> documents = batchTexts.stream()
                .map(document -> Document.builder()
                        .text(document)
                        .metadata("source", "商品") //如果需要设置多个元数据，可以使用map，metadata方法的底层就是map
                        .build())
                .collect(Collectors.toList());
        milvusVectorStore.write(documents);

        List<String> batchTexts02 = new ArrayList<>();
        batchTexts02.add("家用投影仪 1080P高清 自动对焦 5G双频WiFi 兼容4K 白色 便携款");
        batchTexts02.add("电动牙刷 超声波清洁 5种模式 续航90天 软毛刷头 成人款 蓝色");
        // 将documents设置元数据后写到向量数据库中
        List<Document> documents02 = batchTexts02.stream()
                .map(document -> Document.builder()
                        .text(document)
                        .metadata("source", "说明")
                        .build())
                .collect(Collectors.toList());

        milvusVectorStore.write(documents02);
        System.out.println("已写入向量数据库。");
    }

    @Test
    public void testSearch() {
        String query = "无线蓝牙耳机";
        // 设置元数据过滤请求
        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .filterExpression("source=='商品'")
                .topK(2)
                .build();
        // 查询出来的结果是根据score来进行排序的，这个score就是文本的相似度评分，评分越高的越靠前
        List<Document> documents = milvusVectorStore.similaritySearch(searchRequest);
        for (Document document : documents) {
            System.out.println(document.getText());
        }
    }
}
