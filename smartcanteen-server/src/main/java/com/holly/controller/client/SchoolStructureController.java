package com.holly.controller.client;

import com.holly.dto.SchoolTreeDTO;
import com.holly.entity.SchoolStructure;
import com.holly.result.Result;
import com.holly.service.SchoolStructureService;
import com.holly.vo.SchoolTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("clientSchoolStructureController") // 手动起个独特的名字
@RequestMapping("/client/school")
@Tag(name = "校园结构接口")
@RequiredArgsConstructor
public class SchoolStructureController {

    private final SchoolStructureService schoolStructureService;

    @GetMapping("/tree")
    @Operation(summary = "获取全量校园结构树")
    public Result<List<SchoolTreeDTO>> getSchoolTree() {
        // 从 Redis 或数据库获取完整的树形结构
        return Result.success(schoolStructureService.getTree());
    }

    @GetMapping("/nodes")
    @Operation(summary = "按父级ID获取子节点（懒加载）")
    public Result<List<SchoolStructure>> getNodes(@RequestParam(defaultValue = "0") Long parentId) {
        return Result.success(schoolStructureService.getByParentId(parentId));
    }
}