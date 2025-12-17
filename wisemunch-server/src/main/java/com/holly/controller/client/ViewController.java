package com.holly.controller.client;

import com.holly.context.BaseContext;
import com.holly.result.Result;
import com.holly.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.holly.constant.Constants.DISH;

@RestController
@RequestMapping("/client/view")
@RequiredArgsConstructor
public class ViewController {

    private final ViewService viewService;

    /**
     * 菜品浏览次数增加
     * @param dishId
     * @return
     */
    @PostMapping("/increment/dish/{dishId}")
    public Result<String> incrementDishViewCount(@PathVariable Long dishId) {
        Long userId = BaseContext.getUserId();
        viewService.incrementViewCount(dishId, DISH,userId);
        return Result.success();
    }
//
//    /**
//     * 套餐浏览次数增加
//     * @param setmealId
//     * @return
//     */
//    @PostMapping("/increment/setmeal/{setmealId}")
//    public Result<String> incrementSetmealViewCount(@PathVariable Long setmealId) {
//        viewService.incrementViewCount(setmealId, SETMEAL);
//        return Result.success("套餐浏览次数增加成功");
//    }
}