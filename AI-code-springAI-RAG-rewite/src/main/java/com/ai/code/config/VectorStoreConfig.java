package com.ai.code.config;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import io.milvus.client.MilvusServiceClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

    @Autowired
    private DashScopeEmbeddingModel dashScopeEmbeddingModel;

    @Autowired
    private MilvusServiceClient milvusServiceClient;

    @Autowired
    private DashScopeChatModel dashScopeChatModel;             // 阿里云Chat模型

    @Autowired
    private VectorStore milvusVectorStore;                     // 向量存储

    @Autowired
    private DashScopeRerankModel dashScopeRerankModel;

    @Bean
    public VectorStore milvusVectorStore() {
        // 将miluvs的客户端和想要使用的向量模型组合成为一个VectorStore注入到Spring容器中
        return MilvusVectorStore.builder(milvusServiceClient, dashScopeEmbeddingModel).build();
    }

    // 配置ChatClient Bean
    @Bean
    public ChatClient chatClientWithRewrite() {
        return ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(
                        RetrievalAugmentationAdvisor.builder()
                                .documentRetriever(
                                        VectorStoreDocumentRetriever.builder()
                                                .similarityThreshold(.5)
                                                .vectorStore(milvusVectorStore)
                                                .build()
                                )
                                .queryAugmenter(
                                        ContextualQueryAugmenter.builder()
                                                .allowEmptyContext(false)
                                                .emptyContextPromptTemplate(
                                                        PromptTemplate.builder()
                                                                .template("根据您的问题，系统未能找到相关的文档信息。为了更好地帮助您，请提供更多详细信息或尝试重新表述您的问题。")
                                                                .build()
                                                )
                                                .build()
                                )
                                // 配置查询重写转换器
                                .queryTransformers(
                                        RewriteQueryTransformer.builder()
                                                .chatClientBuilder(ChatClient.builder(dashScopeChatModel))
                                                // 设置系统提示，指导LLM如何重写查询
                                                .targetSearchSystem("你是一个词汇清理的专家，主要工作是将用户的模糊问题提取出专业的词汇，以提高向量检索的精度，注意不要有任何多余的解释")
                                                .build()
                                )
                                .build(),
                        new RetrievalRerankAdvisor(milvusVectorStore, dashScopeRerankModel,
                                SearchRequest.builder()
                                        .topK(200)
                                        .similarityThreshold(.4)
                                        .build())
                )
                .build();
    }
}
