package com.holly.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor // 必须有！LangChain4j 实例化对象需要它
@AllArgsConstructor // 方便 Builder 使用
public class AiPurchaseReportVO {
    private String summary;
    private List<AiPurchaseAdviceVO> purchaseList;
}
