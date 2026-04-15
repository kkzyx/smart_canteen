package com.holly.config;

import com.holly.mq.RabbitMqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessQueueConfiguration {

    @Bean
    public DirectExchange orderEventExchange() {
        return new DirectExchange(RabbitMqConstants.ORDER_EVENT_EXCHANGE);
    }

    @Bean
    public Queue orderTimeoutDelayQueue() {
        return QueueBuilder.durable(RabbitMqConstants.ORDER_TIMEOUT_DELAY_QUEUE)
                .ttl(15 * 60 * 1000)
                .deadLetterExchange(RabbitMqConstants.ORDER_EVENT_EXCHANGE)
                .deadLetterRoutingKey(RabbitMqConstants.ORDER_TIMEOUT_RELEASE_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderTimeoutReleaseQueue() {
        return QueueBuilder.durable(RabbitMqConstants.ORDER_TIMEOUT_RELEASE_QUEUE).build();
    }

    @Bean
    public Queue orderPaidQueue() {
        return QueueBuilder.durable(RabbitMqConstants.ORDER_PAID_QUEUE).build();
    }

    @Bean
    public Binding orderTimeoutDelayBinding() {
        return BindingBuilder.bind(orderTimeoutDelayQueue())
                .to(orderEventExchange())
                .with(RabbitMqConstants.ORDER_TIMEOUT_DELAY_ROUTING_KEY);
    }

    @Bean
    public Binding orderTimeoutReleaseBinding() {
        return BindingBuilder.bind(orderTimeoutReleaseQueue())
                .to(orderEventExchange())
                .with(RabbitMqConstants.ORDER_TIMEOUT_RELEASE_ROUTING_KEY);
    }

    @Bean
    public Binding orderPaidBinding() {
        return BindingBuilder.bind(orderPaidQueue())
                .to(orderEventExchange())
                .with(RabbitMqConstants.ORDER_PAID_ROUTING_KEY);
    }

    @Bean
    public DirectExchange knowledgeExchange() {
        return new DirectExchange(RabbitMqConstants.KNOWLEDGE_EXCHANGE);
    }

    @Bean
    public Queue knowledgeIngestQueue() {
        return QueueBuilder.durable(RabbitMqConstants.KNOWLEDGE_INGEST_QUEUE).build();
    }

    @Bean
    public Binding knowledgeIngestBinding() {
        return BindingBuilder.bind(knowledgeIngestQueue())
                .to(knowledgeExchange())
                .with(RabbitMqConstants.KNOWLEDGE_INGEST_ROUTING_KEY);
    }
}
