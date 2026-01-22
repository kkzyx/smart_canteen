package com.holly.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SchoolTreeDTO {
    private Long id;
    private String name;
    private String code;
    private Integer type;
    private List<SchoolTreeDTO> children = new ArrayList<>();
}