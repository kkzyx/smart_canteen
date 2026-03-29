package com.holly.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PickupAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long schoolId;

    private Long canteenId;

    private Long detailId;

    // ===== 冗余展示字段（非必须，但前端舒服）=====
    private String schoolName;

    private String canteenName;

    private String detailName;

    //是否默认 0否 1是
    private Integer isDefault;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}