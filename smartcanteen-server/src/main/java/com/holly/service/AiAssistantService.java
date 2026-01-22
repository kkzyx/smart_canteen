package com.holly.service;

import com.holly.dto.AiChatDTO;
import com.holly.vo.ChatEventVO;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * AI智能助手服务接口
 */
public interface AiAssistantService {

    /**
     * 处理AI聊天请求
     * @param chatDTO 聊天请求DTO
     * @param userId 用户ID
     * @return AI回复响应
     */
    Flux<ChatEventVO> processChat(AiChatDTO chatDTO, Long userId);

    /**
     * 通用问答处理
     * @param message 用户消息
     * @return 通用回复
     */
    Flux<ChatEventVO> handleGeneralQuestion(String sessionId,String message,Long userId);


    /**
     * 智能推荐菜品
     * @param userId 用户ID
     * @param preference 用户偏好描述
     * @param shopId 店铺ID
     * @return 推荐结果
     */
    List<Map<String, Object>> getSmartRecommendations(Long userId, String preference, Long shopId);


}
