package com.holly.tools;

import com.holly.context.ToolContext;
import com.holly.dto.OrderCancelDTO;
import com.holly.entity.Orders;
import com.holly.exception.BaseException;
import com.holly.mapper.OrderMapper;
import com.holly.result.PageResult;
import com.holly.service.OrderService;
import com.holly.vo.OrderVO;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.holly.constant.MessageConstant.ORDER_CANCEL_FAILED;
import static com.holly.constant.MessageConstant.ORDER_CANCEL_SUCCESS;

@Component
@RequiredArgsConstructor
public class OrderTool {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @Tool("查询用户最近的5个订单")
    public List<Orders> getOrdersByUserId() {
        Long userId = (Long) ToolContext.get(ToolContext.USER_ID);
        List<Orders> ordersByUserId = orderMapper.getOrdersByUserId(userId, 5);
        return ordersByUserId;
    }

    @Tool("查询用户指定状态的5个订单")
    public List<OrderVO> getOrdersByUserIdAndStatus(@P(value = "订单状态") Integer status) {
        PageResult pageResult = orderService.pageQueryUser(1, 5, status);
        List<OrderVO> records = (List<OrderVO>) pageResult.getRecords();
        return records ;
    }

    @Tool("取消订单")
    public String cancelOrder(@P(value = "订单ID") Long orderId, @P(value = "取消原因") String reason) {
        OrderCancelDTO orderCancelDTO = new OrderCancelDTO(orderId, reason);
        try {
            orderService.userCancelById(orderCancelDTO);
            return ORDER_CANCEL_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(ORDER_CANCEL_FAILED);
        }
    }
}
