package com.holly.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishSalesDTO implements Serializable {
    /**
     * 菜品名称
     */
    private String name;

    /**
     * 销量
     */
    private Integer quantity;
}