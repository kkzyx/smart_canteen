package com.holly.mq.consumer;

import com.holly.mq.RabbitMqConstants;
import com.holly.mq.message.KnowledgeBaseIngestMessage;
import com.holly.service.KnowledgeBaseIngestionProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KnowledgeBaseIngestConsumer {

    private final KnowledgeBaseIngestionProcessor knowledgeBaseIngestionProcessor;

    @RabbitListener(queues = RabbitMqConstants.KNOWLEDGE_INGEST_QUEUE)
    public void handleKnowledgeIngest(KnowledgeBaseIngestMessage message) {
        try {
            log.info("开始异步处理知识库文件: {}", message.getFileName());
            knowledgeBaseIngestionProcessor.ingest(
                    message.getFileName(),
                    message.getBucketName(),
                    message.getObjectName(),
                    message.getUrl()
            );
            log.info("知识库异步处理成功: {}", message.getFileName());
        } catch (Exception e) {
            log.error("知识库异步处理失败，已丢弃该消息，fileName={}, bucket={}, object={}",
                    message.getFileName(),
                    message.getBucketName(),
                    message.getObjectName(),
                    e);
        }
    }
}
