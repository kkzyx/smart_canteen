package com.holly.controller.client;

import com.holly.constant.CustomerServiceConstant;
import com.holly.context.BaseContext;
import com.holly.entity.CustomerServiceMessage;
import com.holly.entity.CustomerServiceSession;
import com.holly.result.Result;
import com.holly.service.CustomerServiceService;
import com.holly.vo.CustomerServiceSessionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客服相关接口 - 用户端
 */
@Slf4j
@Tag(name = "客服相关接口")
@RestController("clientCustomerServiceController")
@RequestMapping("/client/customer-service")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;

    /**
     * 创建或获取人工客服会话
     */
    @PostMapping("/session/human")
    @Operation(summary = "创建或获取人工客服会话")
    public Result<CustomerServiceSession> createHumanSession() {
        log.info("用户创建人工客服会话");
        // 这里需要从JWT中获取用户ID，暂时使用固定值
        Long userId = BaseContext.getUserId();

        CustomerServiceSession session = customerServiceService.createOrGetSession(userId, CustomerServiceConstant.SERVICE_TYPE_HUMAN);
        return Result.success(session);
    }

    /**
     * 创建或获取AI助手会话
     */
    @PostMapping("/session/ai")
    @Operation(summary = "创建或获取AI助手会话")
    public Result<CustomerServiceSession> createAISession() {
        log.info("用户创建AI助手会话");
        // 这里需要从JWT中获取用户ID，暂时使用固定值
        Long userId = BaseContext.getUserId();

        CustomerServiceSession session = customerServiceService.createOrGetSession(userId, CustomerServiceConstant.SERVICE_TYPE_AI);
        return Result.success(session);
    }

    /**
     * 获取会话消息列表
     *
     * @param sessionId 会话ID
     */
    @GetMapping("/messages/{sessionId}")
    @Operation(summary = "获取会话消息列表")
    public Result<List<CustomerServiceMessage>> getSessionMessages(@PathVariable String sessionId, Integer type) {
        log.info("获取会话消息列表，会话ID：{}", sessionId);
        List<CustomerServiceMessage> messages = customerServiceService.getSessionMessages(sessionId, type);
        return Result.success(messages);
    }

    /**
     * 获取会话详情
     *
     * @param sessionId 会话ID
     */
    @GetMapping("/session/{sessionId}")
    @Operation(summary = "获取会话详情")
    public Result<CustomerServiceSessionVO> getSessionDetail(@PathVariable String sessionId, String type) {
        log.info("获取会话详情，会话ID：{}", sessionId);
        CustomerServiceSessionVO sessionVO = customerServiceService.getSessionDetail(sessionId);
        return Result.success(sessionVO);
    }

    /**
     * 结束会话
     *
     * @param sessionId 会话ID
     */
    @PutMapping("/session/{sessionId}/end")
    @Operation(summary = "结束会话")
    public Result<?> endSession(@PathVariable String sessionId) {
        log.info("结束会话，会话ID：{}", sessionId);
        customerServiceService.endSession(sessionId);
        return Result.success();
    }
}
