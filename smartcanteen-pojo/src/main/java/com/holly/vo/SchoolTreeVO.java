package com.holly.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolTreeVO implements Serializable {

    private Long id;
    private String name;
    private String code;
    private Integer type;
    /**
     * 子节点列表
     */
    private List<SchoolTreeVO> children;
}