package com.holly.mq.consumer;

import com.holly.constant.MessageConstant;
import com.holly.entity.Orders;
import com.holly.mapper.OrderMapper;
import com.holly.mq.RabbitMqConstants;
import com.holly.mq.message.OrderTimeoutMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutConsumer {

    private final OrderMapper orderMapper;

    @RabbitListener(queues = RabbitMqConstants.ORDER_TIMEOUT_RELEASE_QUEUE)
    public void handleOrderTimeout(OrderTimeoutMessage message) {
        Orders order = orderMapper.getOrderById(message.getOrderId());
        if (order == null) {
            log.warn("超时关单消息对应订单不存在: {}", message.getOrderId());
            return;
        }

        if (!Orders.PENDING_PAYMENT.equals(order.getStatus())) {
            log.info("订单已不处于待支付状态，忽略超时关单: orderId={}, status={}", order.getId(), order.getStatus());
            return;
        }

        Orders update = Orders.builder()
                .id(order.getId())
                .status(Orders.CANCELLED)
                .cancelReason(MessageConstant.ORDER_CANCEL_REASON)
                .cancelTime(LocalDateTime.now())
                .build();
        orderMapper.update(update);
        log.info("RabbitMQ 延时关单成功: {}", order.getId());
    }
}
