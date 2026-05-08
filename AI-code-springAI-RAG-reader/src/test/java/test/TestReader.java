package test;

import com.ai.code.Application;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest(classes = Application.class)
public class TestReader {

    @Test
    public void testReaderPdf(@Value("classpath:files/test2.pdf")Resource resource){
        // PagePdfDocumentReader初始化时接收一个Resource对象
        // Resource是Spring框架的资源抽象，表示文件或网络资源
        // @Value注解会自动注入classpath下的文件资源
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(resource);
        for (Document document : pagePdfDocumentReader.read()) {
            System.out.println(document);
        }
    }

    @Test
    public void testReaderDoc(@Value("classpath:files/test.docx") Resource resource){
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        for (Document document : tikaDocumentReader.read()) {
            System.out.println(document);
        }
    }
}
