package test;

import com.ai.code.Application;
import com.alibaba.cloud.ai.transformer.splitter.RecursiveCharacterTextSplitter;
import com.alibaba.cloud.ai.transformer.splitter.SentenceSplitter;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootTest(classes = Application.class)
public class TestSplitter {

    /**
     * Token拆分
     * @param resource resource
     */
    @Test
    public void test01(@Value("classpath:files/test2.pdf") Resource resource) {
        List<Document> documents = new PagePdfDocumentReader(resource).read();

        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(200, 100, 5, 10000, true);
        List<Document> splitterDocs = tokenTextSplitter.apply(documents);
        System.out.println("原始读取的文档数：" + documents.size() + "====" + "拆分后的文档数" + splitterDocs.size());
        splitterDocs.forEach(System.out::println);
    }

    /**
     * 语义拆分
     * @param resource resource
     */
    @Test
    public void test02(@Value("classpath:files/test2.pdf") Resource resource) {
        List<Document> documents = new PagePdfDocumentReader(resource).read();
        // 参数maxLength表示每个块的最大字符数
        // 当一个句子超过这个长度时，会进行拆分
        SentenceSplitter sentenceSplitter = new SentenceSplitter(200);
        List<Document> applyList = sentenceSplitter.apply(documents);
        System.out.println("原始读取的文档数：" + documents.size() + "====" + "拆分后的文档数" + applyList.size());
        applyList.forEach(System.out::println);
    }

    /**
     * 递归拆分
     * 递归拆分过程：
     *   ○ 第一次：尝试按段落分隔符拆分
     *   ○ 第二次：对大块按换行符拆分
     *   ○ 第三次：对大块按句子拆分
     *   ○ 直至所有块满足大小要求
     * @param resource resource
     */
    @Test
    public void test03(@Value("classpath:files/test2.pdf") Resource resource){
        List<Document> documents = new PagePdfDocumentReader(resource).read();

        // 使用默认配置创建分割器
        // 会自动使用中英文分隔符进行递归拆分
        RecursiveCharacterTextSplitter recursiveSplitter = new RecursiveCharacterTextSplitter();
        List<Document> applyList = recursiveSplitter.apply(documents);
        System.out.println("原始读取的文档数：" + documents.size() + "====" + "拆分后的文档数" + applyList.size());
        applyList.forEach(System.out::println);
    }
}
