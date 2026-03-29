package com.holly.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolStructure {
    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private Integer type; // 1:学校, 2:校区, 3:楼栋
    private Integer sort;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}