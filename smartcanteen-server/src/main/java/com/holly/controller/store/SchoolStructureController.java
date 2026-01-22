package com.holly.controller.store;

import com.holly.dto.SchoolTreeDTO;
import com.holly.entity.SchoolStructure;
import com.holly.result.Result;
import com.holly.service.SchoolStructureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/school")
@RequiredArgsConstructor
@Tag(name = "学校结构管理", description = "提供学校、校区、楼栋的三级联动数据接口")
public class SchoolStructureController {

    private final SchoolStructureService schoolStructureService;

    /**
     * 获取全量树形结构
     * 场景：移动端收货地址选择器初始化时调用
     */
    @GetMapping("/tree")
    @Operation(summary = "获取全量学校树", description = "优先从Redis获取，若无则查询数据库并缓存")
    public Result<List<SchoolTreeDTO>> getSchoolTree() {
        List<SchoolTreeDTO> tree = schoolStructureService.getTree();
        return Result.success(tree);
    }

    /**
     * 新增或修改节点
     * 场景：后台管理添加/修改学校、校区或宿舍楼
     */
    @PostMapping("/save")
    @Operation(summary = "保存或更新节点", description = "操作成功后会自动删除Redis缓存")
    public Result<String> saveOrUpdate(@RequestBody SchoolStructure node) {
        schoolStructureService.saveOrUpdateNode(node);
        return Result.success("操作成功");
    }

    /**
     * 删除节点
     * 场景：后台管理删除某个层级
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除节点", description = "操作成功后会自动删除Redis缓存")
    public Result<String> delete(@PathVariable Long id) {
        schoolStructureService.deleteNode(id);
        return Result.success("删除成功");
    }
}