package com.holly.controller.store;

import com.holly.entity.CustomerServiceMessage;
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
 * 客服相关接口 - 管理端
 */
@Slf4j
@Tag(name = "客服管理接口")
@RestController("storeCustomerServiceController")
@RequestMapping("/store/customer-service")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceService customerServiceService;

    /**
     * 获取所有进行中的人工客服会话
     */
    @GetMapping("/sessions/active")
    @Operation(summary = "获取所有进行中的人工客服会话")
    public Result<List<CustomerServiceSessionVO>> getActiveSessions() {
        log.info("获取所有进行中的人工客服会话");
        List<CustomerServiceSessionVO> sessions = customerServiceService.getActiveHumanSessions();
        return Result.success(sessions);
    }

    /**
     * 获取客服人员的会话列表
     * @param staffId 客服人员ID
     */
    @GetMapping("/sessions/staff/{staffId}")
    @Operation(summary = "获取客服人员的会话列表")
    public Result<List<CustomerServiceSessionVO>> getStaffSessions(@PathVariable Long staffId) {
        log.info("获取客服人员的会话列表，客服ID：{}", staffId);
        List<CustomerServiceSessionVO> sessions = customerServiceService.getStaffSessions(staffId);
        return Result.success(sessions);
    }

    /**
     * 分配客服人员
     * @param sessionId 会话ID
     * @param staffId 客服人员ID
     */
    @PutMapping("/session/{sessionId}/assign/{staffId}")
    @Operation(summary = "分配客服人员")
    public Result<?> assignStaff(@PathVariable String sessionId, @PathVariable Long staffId) {
        log.info("分配客服人员，会话ID：{}，客服ID：{}", sessionId, staffId);
        customerServiceService.assignStaff(sessionId, staffId);
        return Result.success();
    }

    /**
     * 获取会话消息列表
     * @param sessionId 会话ID
     */
    @GetMapping("/messages/{sessionId}")
    @Operation(summary = "获取会话消息列表")
    public Result<List<CustomerServiceMessage>> getSessionMessages(@PathVariable String sessionId,Integer type) {
        log.info("获取会话消息列表，会话ID：{}", sessionId);
        List<CustomerServiceMessage> messages = customerServiceService.getSessionMessages(sessionId,type);
        return Result.success(messages);
    }

    /**
     * 获取会话详情
     * @param sessionId 会话ID
     */
    @GetMapping("/session/{sessionId}")
    @Operation(summary = "获取会话详情")
    public Result<CustomerServiceSessionVO> getSessionDetail(@PathVariable String sessionId) {
        log.info("获取会话详情，会话ID：{}", sessionId);
        CustomerServiceSessionVO sessionVO = customerServiceService.getSessionDetail(sessionId);
        return Result.success(sessionVO);
    }

    /**
     * 结束会话
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
