package com.holly.service;

import com.holly.query.SensitivePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.result.Result;

import java.util.List;

public interface SensitiveService {

    /**
     * 新增敏感词
     *
     * @param sensitive
     */
    Result addSensitive(String sensitive);

    /**
     * 修改敏感词
     * @param id
     * @param sensitive
     * @return
     */
    Result updateSensitive(Integer id, String sensitive);

    /**
     * 分页查询敏感词
     * @param dto
     * @return
     */
    PageResult page(SensitivePageQueryDTO dto);

    /**
     * 删除敏感词
     * @param ids
     * @return
     */
    Result deleteByIds(List<Long> ids);
}
