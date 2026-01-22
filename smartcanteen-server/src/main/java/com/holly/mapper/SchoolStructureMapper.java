package com.holly.mapper;

import com.holly.entity.SchoolStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SchoolStructureMapper {

    // 获取所有节点 (用于 Service 层在内存中构建树)
    List<SchoolStructure> selectAll();

    // 根据 ID 查询
    SchoolStructure selectById(@Param("id") Long id);

    // 插入新节点
    int insert(SchoolStructure schoolStructure);

    // 更新节点
    int update(SchoolStructure schoolStructure);

    // 删除节点
    int deleteById(@Param("id") Long id);
}