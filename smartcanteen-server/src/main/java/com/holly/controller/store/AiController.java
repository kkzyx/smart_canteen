package com.holly.controller.store;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.holly.chatService.AiPurchaseAdviceService;
import com.holly.dto.DishSalesDTO;
import com.holly.mapper.OrderMapper;
import com.holly.result.Result;
import com.holly.service.impl.AiPurchaseServiceImpl;
import com.holly.vo.AiPurchaseAdviceVO;
import com.holly.vo.AiPurchaseReportVO;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "AI相关接口")
@RequestMapping("/store/ai")
public class AiController {


    private final AiPurchaseServiceImpl aiPurchaseService;


    private final OrderMapper orderMapper;



    /**
     * 获取指定日期的AI采购建议报告
     * @param date 格式：YYYY-MM-DD
     * @return 包含摘要和食材清单的报告对象
     */
    @GetMapping("/purchase-advice")
    @Operation(summary = "获取AI智能采购建议")
    public Result<AiPurchaseReportVO> getAiPurchaseAdvice(
            @RequestParam(required = false) String date) {

        log.info("管理端触发AI采购分析，目标日期：{}", date);

        // 1. 如果前端没传日期，默认查询 2025-08-28 (你的测试数据日期)
        // 实际生产环境下可以改为 LocalDate.now().minusDays(1).toString()
        if (date == null || date.isEmpty()) {
            date = "2025-08-28";
        }

        try {
            // 2. 调用 Service 执行业务逻辑（查库 -> 喂给AI -> 解析对象）
            AiPurchaseReportVO report = aiPurchaseService.generateReport(date);

            // 3. 返回结构化结果
            return Result.success(report);

        } catch (Exception e) {
            log.error("AI分析过程发生异常: {}", e.getMessage());

            // 4. 针对超时或AI解析异常进行友好提示
            if (e.getMessage().contains("timeout")) {
                return Result.error("AI 思考时间过长，请稍后刷新重试");
            }
            return Result.error("AI 助手暂时走神了，请检查网络或稍后再试");
        }
    }

}