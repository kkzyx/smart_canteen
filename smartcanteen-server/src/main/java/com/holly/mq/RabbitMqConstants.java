package com.holly.mq;

public final class RabbitMqConstants {

    private RabbitMqConstants() {
    }

    public static final String ORDER_EVENT_EXCHANGE = "smartcanteen.order.event.exchange";
    public static final String ORDER_TIMEOUT_DELAY_QUEUE = "smartcanteen.order.timeout.delay.queue";
    public static final String ORDER_TIMEOUT_RELEASE_QUEUE = "smartcanteen.order.timeout.release.queue";
    public static final String ORDER_TIMEOUT_DELAY_ROUTING_KEY = "order.timeout.delay";
    public static final String ORDER_TIMEOUT_RELEASE_ROUTING_KEY = "order.timeout.release";

    public static final String ORDER_PAID_QUEUE = "smartcanteen.order.paid.queue";
    public static final String ORDER_PAID_ROUTING_KEY = "order.paid";

    public static final String KNOWLEDGE_EXCHANGE = "smartcanteen.knowledge.exchange";
    public static final String KNOWLEDGE_INGEST_QUEUE = "smartcanteen.knowledge.ingest.queue";
    public static final String KNOWLEDGE_INGEST_ROUTING_KEY = "knowledge.ingest";
}
