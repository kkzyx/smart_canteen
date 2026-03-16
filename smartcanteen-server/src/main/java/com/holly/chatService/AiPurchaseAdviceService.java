package com.holly.chatService;

import com.holly.vo.AiPurchaseAdviceVO;
import com.holly.vo.AiPurchaseReportVO;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

import java.util.List;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel"
//        streamingChatModel = "openAiStreamingChatModel"
)
public interface AiPurchaseAdviceService {
    /**
     * @param salesData 销量统计数据的字符串描述
     * @param day 统计日期（如“昨天”）
     */
    @SystemMessage("""
            你是一位资深的餐饮采购专家。
            请根据用户提供的菜品销量数据，分析明天需要的食材采购清单。
            要求：
            1. 考虑10%的备货冗余。
            2. 必须以 JSON 数组格式返回，JSON 字段必须包含：
               "ingredient" (食材名称), "amount" (数量), "unit" (单位), "reason" (理由)。
            """)
    TokenStream getAdvice(@UserMessage String salesData, @V("day") String day);
//    List<AiPurchaseAdviceVO> getAdvice(@UserMessage String salesData, @V("day") String day);
// 在 AiPurchaseAdviceService.java 中增加
    @SystemMessage("""
        你是一位精通餐饮成本控制与食材配比的首席采购官。
        请根据提供的销量数据，生成一份专业的“次日采购建议报告”。
    
        【报告结构要求】
        请严格返回一个 JSON 对象（不要返回数组，也不要 Markdown 代码块），包含以下两个字段：
        
        1. "summary": 
           - 格式：一段简洁的文字。
           - 内容：包含查询日期、卖出的菜品统计（如：2025年08月28日，共售出白灼菜心3份、清炒上海青1份等）。
        
        2. "purchase_list": 
           - 格式：JSON 数组。
           - 数组每个对象包含：
             - ingredient: 食材原件名称（如“菜心”、“五花肉”）。
             - amount: 换算后的建议采购数字（考虑10%冗余并向上取整）。
             - unit: 采购单位（如“颗”、“斤”、“千克”）。
             - reason: 极其简练的换算依据（如：3份菜心需1.5颗，冗余后取整为2颗）。
    
        【换算逻辑参考】
        - 蔬菜：1颗大菜心做2份；3颗小上海青做1份。
        - 肉类：1份小炒肉需150g猪肉。
        - 冗余：所有食材在折算后额外增加 10% 备货量。
        - 取整：最终采购量必须为整数（向上取整）。
    
        【注意事项】
        - 严禁在字段外包含任何解释性文字。
        - 确保 summary 语言专业且易读。
        """)
    String getAdviceSync(@UserMessage String salesData, @V("day") String day);

    @SystemMessage(fromResource = "PurchaseRecommend.txt")
    AiPurchaseReportVO getReport(@UserMessage String salesData, @V("day") String day);
    }
