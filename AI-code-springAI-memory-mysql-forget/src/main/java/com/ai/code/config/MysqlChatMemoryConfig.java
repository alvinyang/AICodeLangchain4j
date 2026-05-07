package com.ai.code.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class MysqlChatMemoryConfig {

    @Autowired
    private JdbcChatMemoryRepository chatMemoryRepository;

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                .requestFactory(
                        ClientHttpRequestFactories.get(
                                JdkClientHttpRequestFactory.class,
                                ClientHttpRequestFactorySettings.DEFAULTS
                                        .withConnectTimeout(Duration.ofMinutes(3)) //设定连接超时时间位3分钟
                                        .withReadTimeout(Duration.ofMinutes(3))    //设定获取结果超时时间为3分钟
                        )
                );
    }

    @Bean
    public ChatMemory jdbcChatMemory(){
        return MessageWindowChatMemory.builder().maxMessages(10).chatMemoryRepository(chatMemoryRepository).build();
    }

    @Bean(value = "memoryChatClient")
    public ChatClient chatClient(DashScopeChatModel dashScopeChatModel, ChatMemory chatMemory){
        return ChatClient.builder(dashScopeChatModel)
                .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean(value = "digestClient")
    public ChatClient digestClient(DashScopeChatModel dashScopeChatModel, ChatMemory chatMemory){
        return ChatClient.builder(dashScopeChatModel).defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem("""
                   你是一位专业的对话摘要专家，任务是阅读完整的对话历史（用户与AI的往复消息），然后生成一段**简洁、连贯、包含关键意图和事实**的摘要。

                   核心要求：
                   1. 优先保留**用户的核心目标、问题、需求、情感倾向**和**关键事实/决定**，而非AI的礼貌回复或重复解释。
                   2. 按时间顺序组织内容，体现对话的进展和演变。
                   3. 使用**第三人称叙述**，语言客观、中性、精炼。
                   4. 突出**未解决的问题、待办事项、用户反复强调的点**，以及**最新的用户意图**。
                   5. 长度控制在 **80–300 字**（中文），尽量压缩但不丢失重要上下文。
                   6. **不要添加**任何你自己的判断、建议或额外信息，只总结已有对话内容。
                   7. 输出**仅包含摘要正文**，不要出现“摘要：”、“以下是摘要”等前缀。

                   示例输出风格：
                   用户正在开发一个Spring AI聊天机器人，需要实现带摘要的长期记忆机制。他询问了MessageWindowChatMemory的局限性，并希望结合token窗口和自动摘要来控制上下文长度。目前讨论到了自定义SummarizingChatMemory的实现思路，以及使用Advisor在prompt构建前插入摘要的方案。用户倾向于用一个小模型专门做摘要以节省成本，下一步计划实现一个带重要性打分的遗忘策略。
                   """)
                .build();
    }
}
