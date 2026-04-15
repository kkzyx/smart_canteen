package com.holly.mq.producer;

import com.holly.mq.RabbitMqConstants;
import com.holly.mq.message.KnowledgeBaseIngestMessage;
import com.holly.mq.message.OrderPaidMessage;
import com.holly.mq.message.OrderTimeoutMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class BusinessEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderTimeoutMessageAfterCommit(OrderTimeoutMessage message) {
        sendAfterCommit(() -> rabbitTemplate.convertAndSend(
                RabbitMqConstants.ORDER_EVENT_EXCHANGE,
                RabbitMqConstants.ORDER_TIMEOUT_DELAY_ROUTING_KEY,
                message
        ));
    }

    public void sendOrderPaidMessageAfterCommit(OrderPaidMessage message) {
        sendAfterCommit(() -> rabbitTemplate.convertAndSend(
                RabbitMqConstants.ORDER_EVENT_EXCHANGE,
                RabbitMqConstants.ORDER_PAID_ROUTING_KEY,
                message
        ));
    }

    public void sendKnowledgeIngestMessageAfterCommit(KnowledgeBaseIngestMessage message) {
        sendAfterCommit(() -> rabbitTemplate.convertAndSend(
                RabbitMqConstants.KNOWLEDGE_EXCHANGE,
                RabbitMqConstants.KNOWLEDGE_INGEST_ROUTING_KEY,
                message
        ));
    }

    private void sendAfterCommit(Runnable action) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            action.run();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                action.run();
            }
        });
    }
}
