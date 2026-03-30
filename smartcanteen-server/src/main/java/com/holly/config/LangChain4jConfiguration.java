package com.holly.config;

import com.holly.chatService.ConsultantService;
import com.holly.repository.RedisChatMemoryStore;
import com.holly.tools.DishRecommendTool;
import com.holly.tools.OrderTool;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.milvus.MilvusEmbeddingStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.holly.constant.CustomerServiceConstant.REDIS_KEY_SESSION_MESSAGES;

@Configuration
public class LangChain4jConfiguration {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(@Value("${training.milvus.uri}") String uri,
                                                      @Value("${training.milvus.token}") String token,
                                                      @Value("${training.milvus.collection-name}") String collectionName,
                                                      @Value("${training.milvus.dimension}") Integer dimension) {
        return MilvusEmbeddingStore.builder()
                .uri(uri)
                .token(token)
                .collectionName(collectionName)
                .dimension(dimension)
                .autoFlushOnInsert(true)
                .build();
    }

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
                                               ObjectProvider<ContentRetriever> contentRetrieverProvider) {

        var builder = AiServices.builder(ConsultantService.class)
                .chatModel(openAiChatModel)
                .streamingChatModel(openAiStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(dishRecommendTool, orderTool);

        contentRetrieverProvider.ifAvailable(builder::contentRetriever);

        return builder.build();
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();
    }
}
