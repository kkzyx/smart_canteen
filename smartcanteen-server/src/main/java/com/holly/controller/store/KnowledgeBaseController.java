package com.holly.controller.store;

import com.holly.exception.FileException;
import com.holly.query.KnowledgeBasePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;
import com.holly.service.KnowledgeBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.holly.constant.MessageConstant.FILE_NOT_SELECTED;

@RestController
@RequestMapping("/store/knowledgeBase")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "向量存储相关接口")
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @PostMapping("/embed")
    @Operation(summary = "新增知识库")
    public Result addKnowledgeBase(@RequestPart("file") MultipartFile file) throws Exception {
        log.info("新增知识库 ==> {}", file);
        if (file.isEmpty()) {
            throw new FileException(FILE_NOT_SELECTED);
        }
        String result = knowledgeBaseService.addKnowledgeBase(file);
        return Result.success(result);
    }

    @GetMapping("/page")
    @Operation(summary = "知识库分页查询")
    public Result<PageResult> page(KnowledgeBasePageQueryDTO dto) {
        log.info("分页查询知识库 ==> {}", dto);
        PageResult pageResult = knowledgeBaseService.page(dto);
        return Result.success(pageResult);
    }

    /**
     * 批量删除知识库
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除知识库")
    public Result batchDelete(@RequestParam List<Integer> ids) {
        log.info("删除知识库 ==> {}", ids);
        int i = knowledgeBaseService.deleteByIds(ids);
        if (i <= 0) {
            return Result.error();
        }
        return Result.success();
    }
}
