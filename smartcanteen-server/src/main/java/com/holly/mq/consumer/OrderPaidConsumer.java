package com.holly.mq.consumer;

import com.alibaba.fastjson2.JSON;
import com.holly.constant.CachePrefixConstant;
import com.holly.entity.OrderDetail;
import com.holly.mq.RabbitMqConstants;
import com.holly.mq.message.OrderPaidMessage;
import com.holly.mapper.DishMapper;
import com.holly.mapper.OrderDetailMapper;
import com.holly.mapper.SetmealMapper;
import com.holly.websocket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.holly.constant.CachePrefixConstant.USER_ORDER;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPaidConsumer {

    private final WebSocketServer webSocketServer;
    private final OrderDetailMapper orderDetailMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @RabbitListener(queues = RabbitMqConstants.ORDER_PAID_QUEUE)
    public void handleOrderPaid(OrderPaidMessage message) {
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(message.getOrderId());
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getDishId() != null) {
                dishMapper.updateHotDish(orderDetail.getDishId());
            }
            if (orderDetail.getSetmealId() != null) {
                setmealMapper.updateHotSetmeal(orderDetail.getSetmealId());
            }
        }

        clearUserOrderCache(message.getUserId());
        clearDishCache();

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", 1);
        payload.put("orderId", message.getOrderId());
        payload.put("content", "订单号" + message.getOrderNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(payload));

        log.info("订单支付后的异步通知与统计处理完成: orderId={}", message.getOrderId());
    }

    private void clearUserOrderCache(Long userId) {
        Set<String> keys = redisTemplate.keys(USER_ORDER + userId + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private void clearDishCache() {
        Set<String> dishKeys = redisTemplate.keys(CachePrefixConstant.DISH_KEY + "*");
        if (dishKeys != null && !dishKeys.isEmpty()) {
            redisTemplate.delete(dishKeys);
        }

        Set<String> setmealKeys = redisTemplate.keys(CachePrefixConstant.SETMEAL_KEY + "*");
        if (setmealKeys != null && !setmealKeys.isEmpty()) {
            redisTemplate.delete(setmealKeys);
        }

        Set<String> recommendKeys = redisTemplate.keys(CachePrefixConstant.RECOMMEND_DISH_KEY + "*");
        if (recommendKeys != null && !recommendKeys.isEmpty()) {
            redisTemplate.delete(recommendKeys);
        }
    }
}
