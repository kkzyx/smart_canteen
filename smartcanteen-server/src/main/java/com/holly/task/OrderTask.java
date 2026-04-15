package com.holly.task;

import com.holly.constant.MessageConstant;
import com.holly.entity.Orders;
import com.holly.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * @description 订单定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTask {

    private final OrderMapper orderMapper;

    @Value("${training.rabbitmq.delay-close-enabled:true}")
    private boolean delayCloseEnabled;

    /**
     * 处理超时未支付订单。
     * 当启用 RabbitMQ 延时关单时，该定时任务退化为兜底开关，避免重复关单。
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder() {
        if (delayCloseEnabled) {
            log.info("已启用 RabbitMQ 延时关单，跳过定时扫表关单任务");
            return;
        }

        log.info("开始处理超时订单 => {}", LocalDateTime.now());

        Orders order = Orders.builder()
                .status(Orders.CANCELLED)
                .cancelReason(MessageConstant.ORDER_CANCEL_REASON)
                .cancelTime(LocalDateTime.now())
                .build();

        processOrders(Orders.PENDING_PAYMENT, -15, order);
    }

    /**
     * 每天凌晨处理长时间处于派送中的订单。
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("开始处理派送中的订单 => {}", LocalDateTime.now());

        Orders order = Orders.builder()
                .status(Orders.COMPLETED)
                .build();

        processOrders(Orders.DELIVERY_IN_PROGRESS, -60, order);
    }

    private void processOrders(Integer status, int timeoutTime, Orders order) {
        LocalDateTime time = LocalDateTime.now().plusMinutes(timeoutTime);
        List<Orders> orderList = orderMapper.getOrdersByStatusAndOrderTimeLT(status, time);
        if (orderList == null || orderList.isEmpty()) {
            return;
        }

        Integer orderStatus = order.getStatus();
        log.info(
                Orders.CANCELLED.equals(orderStatus) ? "超时自动取消的订单数量 ==> {}" : "长时间派送中的订单数量 ==> {}",
                orderList.size()
        );

        orderMapper.updateOrdersStatus(orderList, order);
    }

    private void processOrders(Integer status, int timeoutTime, Consumer<Orders> updateOrder) {
        LocalDateTime time = LocalDateTime.now().plusMinutes(timeoutTime);
        List<Orders> orderList = orderMapper.getOrdersByStatusAndOrderTimeLT(status, time);
        if (orderList == null || orderList.isEmpty()) {
            return;
        }

        orderList.forEach(updateOrder);
        orderList.forEach(orderMapper::update);
    }
}
