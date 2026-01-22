package com.holly.service.impl;

import com.holly.dto.SchoolTreeDTO;
import com.holly.entity.SchoolStructure;
import com.holly.mapper.SchoolStructureMapper;
import com.holly.service.SchoolStructureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
//@CacheConfig(cacheNames = "school::tree")
public class SchoolStructureServiceImpl implements SchoolStructureService {

    private final SchoolStructureMapper mapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "school:tree";

    /**
     * 获取全量树形结构 (给前端选择器使用)
     */
    public List<SchoolTreeDTO> getTree() {
        // 1. 从缓存获取
        List<SchoolTreeDTO> cache = (List<SchoolTreeDTO>) redisTemplate.opsForValue().get(CACHE_KEY);
        if (cache != null) {
            log.info("从Redis获取学校树...");
            return cache;
        }

        // 2. 从数据库查出所有数据
        List<SchoolStructure> all = mapper.selectAll();

        // 3. 构建树形逻辑
        List<SchoolTreeDTO> tree = buildTree(all, 0L);

        // 4. 存入Redis (过期时间建议长一点，因为这部分数据变动极小)
        redisTemplate.opsForValue().set(CACHE_KEY, tree, 24, TimeUnit.HOURS);
        return tree;
    }

    /**
     * 新增/修改/删除时清理缓存
     */
    /**
     * 新增/修改时清理缓存
     */
    @Transactional
    public void saveOrUpdateNode(SchoolStructure node) {
        if (node.getId() == null) {
            mapper.insert(node);
        } else {
            mapper.update(node);
        }
        // 操作成功后删除 Redis 缓存
        redisTemplate.delete(CACHE_KEY);
    }

    /**
     * 删除节点时清理缓存
     */
    @Transactional
    public void deleteNode(Long id) {
        mapper.deleteById(id);
        redisTemplate.delete(CACHE_KEY);
    }

    // 递归构建树
    private List<SchoolTreeDTO> buildTree(List<SchoolStructure> list, Long parentId) {
        return list.stream()
                .filter(item -> item.getParentId().equals(parentId))
                .map(item -> {
                    SchoolTreeDTO dto = new SchoolTreeDTO();
                    BeanUtils.copyProperties(item, dto);
                    dto.setChildren(buildTree(list, item.getId())); // 递归
                    return dto;
                }).collect(Collectors.toList());
    }
}
