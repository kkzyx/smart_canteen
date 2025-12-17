package com.holly.config;

import com.holly.chatService.ConsultantService;
import com.holly.repository.RedisChatMemoryStore;
import com.holly.tools.DishRecommendTool;
import com.holly.tools.OrderTool;
import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.holly.constant.CustomerServiceConstant.REDIS_KEY_SESSION_MESSAGES;


@Configuration
public class LangChain4jConfiguration {

    @Autowired(required = false)
    private RedisEmbeddingStore redisEmbeddingStore;
    
    @Autowired
    private EmbeddingModel embeddingModel;

    //创建会话提供者
    @Bean
    public ChatMemoryProvider chatMemoryProvider(RedisChatMemoryStore store) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(REDIS_KEY_SESSION_MESSAGES + memoryId)
                .maxMessages(20)
                .chatMemoryStore(store)
                .build();
    }

    @Bean
    public ConsultantService consultantService(OpenAiChatModel openAiChatModel,
                                               OpenAiStreamingChatModel openAiStreamingChatModel,
                                               ChatMemoryProvider chatMemoryProvider,
                                               DishRecommendTool dishRecommendTool,
                                               OrderTool orderTool,
                                               //McpToolProvider toolProvider,
                                               ObjectProvider<ContentRetriever> contentRetrieverProvider) {

        var builder = AiServices.builder(ConsultantService.class)
                //聊天模型
                .chatModel(openAiChatModel)
                //流式聊天模型
                .streamingChatModel(openAiStreamingChatModel)
                //会话提供者
                .chatMemoryProvider(chatMemoryProvider)
                //工具
                .tools(dishRecommendTool, orderTool);
                //mcp
                //.toolProvider(toolProvider)
        
        // 如果 ContentRetriever 可用，则添加检索增强
        contentRetrieverProvider.ifAvailable(builder::contentRetriever);
        
        return builder.build();
    }

    //mcp服务
//    @Bean
//    public McpToolProvider toolProvider() {
//
//        McpTransport transport = new StdioMcpTransport.Builder()
//                .command(List.of("npx.cmd", "-y", "@baidumap/mcp-server-baidu-map"))
//                .environment(Map.of("BAIDU_MAP_API_KEY", System.getenv("BAIDU_KAIFANG_API_KEY")))
//                .logEvents(true)
//                .build();
//
//        McpClient mcpClient = new DefaultMcpClient.Builder()
//                .transport(transport)
//                .build();
//
//        return McpToolProvider.builder()
//                .mcpClients(mcpClient)
//                .build();
//    }

    //构建向量数据库检索对象
    @Bean
    @ConditionalOnBean(RedisEmbeddingStore.class)
    public ContentRetriever contentRetriever() {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(redisEmbeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();
    }
}
