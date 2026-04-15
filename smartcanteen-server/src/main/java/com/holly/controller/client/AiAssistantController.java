package com.holly.controller.client;

import com.holly.context.BaseContext;
import com.holly.context.ToolContext;
import com.holly.dto.AiChatDTO;
import com.holly.dto.RecommendRequestDTO;
import com.holly.exception.AiException;
import com.holly.result.Result;
import com.holly.service.AiAssistantService;
import com.holly.vo.ChatEventVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client/ai-assistant")
@Tag(name = "AI智能助手相关接口")
@RequiredArgsConstructor
@Slf4j
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "AI聊天")
    public Flux<ChatEventVO> chat(@RequestBody AiChatDTO chatDTO) {
        log.info("AI聊天请求: {}", chatDTO);
        if (chatDTO.getSessionId() == null || chatDTO.getSessionId().trim().isEmpty()) {
            log.error("AI聊天请求缺少sessionId");
            throw new AiException("会话ID不能为空");
        }

        Long userId = BaseContext.getUserId();
        ToolContext.put(ToolContext.ORIGINAL_USER_MESSAGE, chatDTO.getMessage());
        return aiAssistantService.processChat(chatDTO, userId);
    }

    @PostMapping("/recommend")
    @Operation(summary = "AI智能推荐菜品")
    public Result<List<Map<String, Object>>> recommendDishes(@RequestBody RecommendRequestDTO requestDTO) {
        log.info("AI智能推荐菜品请求: {}", requestDTO);
        Long userId = BaseContext.getUserId();
        try {
            List<Map<String, Object>> recommendations =
                    aiAssistantService.getSmartRecommendations(userId, requestDTO.getPreference(), requestDTO.getShopId());
            return Result.success(recommendations);
        } catch (Exception e) {
            log.error("AI推荐菜品失败", e);
            return Result.error("推荐失败，请稍后重试");
        }
    }
}
