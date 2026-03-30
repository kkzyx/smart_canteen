package com.holly.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBase {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 向量分段 ids
     */
    private String vectorIds;

    /**
     * 原始文件 url
     */
    private String url;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
