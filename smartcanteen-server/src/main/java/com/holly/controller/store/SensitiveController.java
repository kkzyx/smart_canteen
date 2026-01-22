package com.holly.controller.store;

import com.holly.query.SensitivePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.SensitiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensitive")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "敏感词接口")
public class SensitiveController {
    private final SensitiveService sensitiveService;

    /**
     * 新增敏感词
     */
    @PostMapping
    @Operation(summary = "新增敏感词")
    public Result addSensitive(@RequestParam String sensitive) {
        log.info("新增敏感词==> {}", sensitive);
        return sensitiveService.addSensitive(sensitive);
    }

    /**
     * 修改敏感词
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改敏感词")
    public Result updateSensitive(@PathVariable Integer id, @RequestParam String sensitive) {
        log.info("修改敏感词==> id:{},修改词语：{}", id, sensitive);
        return sensitiveService.updateSensitive(id, sensitive);
    }

    /**
     * 分页查询敏感词
     */
    @GetMapping
    @Operation(summary = "分页查询敏感词")
    public Result<PageResult> page(SensitivePageQueryDTO dto) {
        log.info("分页查询敏感词==> SensitivePageQueryDTO:{},", dto);
        PageResult page = sensitiveService.page(dto);
        return Result.success(page);
    }

    /**
     * 删除敏感词
     */
    @DeleteMapping
    @Operation(summary = "删除敏感词")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("删除敏感词==> {}", ids);
        return sensitiveService.deleteByIds(ids);
    }
}
