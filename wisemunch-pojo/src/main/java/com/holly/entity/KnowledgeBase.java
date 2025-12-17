package com.holly.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
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
     * 单文件关联的redis向量ids
     */
    private String redisIds;
    /**
     * 原始文件的url
     */
    private String url;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
}
