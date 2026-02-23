package com.holly.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.holly.chatService.AiPurchaseAdviceService;

import com.holly.dto.DishSalesDTO;
import com.holly.mapper.OrderMapper;
import com.holly.vo.AiPurchaseReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import static com.holly.constant.CachePrefixConstant.AI_PURCHASE_ADVICE;

@Slf4j
@Service
public class AiPurchaseServiceImpl {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AiPurchaseAdviceService aiService;
    // 建议直接注入或 new 一个 ObjectMapper，它能处理对象转换
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public AiPurchaseReportVO generateReport(String date) {
        String cacheKey = AI_PURCHASE_ADVICE + date;
// 1. 从 Redis 取出原始数据（由于没改配置，这里拿到的多半是 LinkedHashMap）
        Object cached = redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
            log.info("从缓存中获取数据，执行手动类型转换: {}", date);
            try {
                // 如果是直接对象则直接返回，如果是 Map 则转换
                if (cached instanceof AiPurchaseReportVO) {
                    return (AiPurchaseReportVO) cached;
                }
                // 【核心代码】利用 Jackson 将 LinkedHashMap 转为 VO 对象
                return objectMapper.convertValue(cached, AiPurchaseReportVO.class);
            } catch (Exception e) {
                log.error("缓存数据转换失败，准备重新请求 AI", e);
                // 转换失败可以考虑删掉旧缓存，让程序往下走重新生成
                redisTemplate.delete(cacheKey);
            }
        }

        // 2. 查数据库并请求 AI
        // 假设传进来的 date 是 "2025-02-28"
        LocalDate targetDate = LocalDate.parse(date);
        // 获取该日期的 00:00:00
        LocalDateTime begin = targetDate.atStartOfDay();
        // 获取该日期的 23:59:59.999999999
        LocalDateTime end = LocalDateTime.of(targetDate, LocalTime.MAX);

        List<DishSalesDTO> sales = orderMapper.getSalesStatistics(begin, end);

        if (sales == null || sales.isEmpty()) {
            return null; // 或者抛出异常由全局处理
        }

        String salesSummary = sales.stream()
                .map(s -> s.getName() + ":" + s.getQuantity() + "份")
                .collect(Collectors.joining("，"));
        AiPurchaseReportVO report = aiService.getReport(salesSummary, date);
        // 3. 存入 Redis，有效期 12 小时（因为一天的销量是不变的）
        if (report != null) {
            redisTemplate.opsForValue().set(cacheKey, report, 12, TimeUnit.HOURS);
        }

        return report;
    }
}