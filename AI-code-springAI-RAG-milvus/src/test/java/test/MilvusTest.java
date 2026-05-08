package test;

import com.ai.code.Application;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = Application.class)
public class MilvusTest {

    @Autowired
    private VectorStore milvusVectorStore;

    @Test
    public void test01(){
        List<String> batchTexts = new ArrayList<>();
        batchTexts.add("【2025新款】智能保温杯 316不锈钢 温度显示 24小时保温 白色 500ml");
        batchTexts.add("无线蓝牙耳机 半入耳式 降噪高清通话 续航40小时 适配安卓/苹果");
        batchTexts.add("机械键盘 青轴 104键 背光有线 电竞游戏专用 全键无冲 黑色");
        batchTexts.add("家用投影仪 1080P高清 自动对焦 5G双频WiFi 兼容4K 白色 便携款");
        batchTexts.add("电动牙刷 超声波清洁 5种模式 续航90天 软毛刷头 成人款 蓝色");
        // 将documents写到向量数据库中
        List<Document> documents = batchTexts.stream().map(Document::new).collect(Collectors.toList());
        milvusVectorStore.write(documents);
        System.out.println("已写入向量数据库。");
    }

    /**
     * 查询的结果示例：查询出来的结果是根据score来进行排序的，这个score就是文本的相似度评分，评分越高的越靠前
     * Document{id='15ad2278-816d-462a-9ae1-10094f8db195', text='无线蓝牙耳机 半入耳式 降噪高清通话 续航40小时 适配安卓/苹果', media='null', metadata={distance=0.20009982585906982}, score=0.7999001741409302}
     * Document{id='c3123e9f-e548-428f-b37b-032684ca59f2', text='电动牙刷 超声波清洁 5种模式 续航90天 软毛刷头 成人款 蓝色', media='null', metadata={distance=0.5873018503189087}, score=0.4126981198787689}
     * Document{id='1c12bc10-af9d-4da7-94a3-52efce5f5535', text='家用投影仪 1080P高清 自动对焦 5G双频WiFi 兼容4K 白色 便携款', media='null', metadata={distance=0.615007221698761}, score=0.384992778301239}
     * Document{id='6e461a69-9a2f-496e-ae0f-5be3f5253e66', text='机械键盘 青轴 104键 背光有线 电竞游戏专用 全键无冲 黑色', media='null', metadata={distance=0.6246597766876221}, score=0.3753402531147003}
     */
    @Test
    public void test02() {
        String query = "无线蓝牙耳机";
        // 查询出来的结果是根据score来进行排序的，这个score就是文本的相似度评分，评分越高的越靠前
        List<Document> documents = milvusVectorStore.similaritySearch(query);
        for (Document document : documents) {
            System.out.println(document);
        }
    }
}
