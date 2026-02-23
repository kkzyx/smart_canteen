package com.holly.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 必须有！LangChain4j 实例化对象需要它
@AllArgsConstructor // 方便 Builder 使用
public class AiPurchaseAdviceVO {
    private String ingredient;    // 食材名称
    private Integer amount;        // 建议采购量
    private String unit;          // 单位
    private String reason;        // 采购理由
}