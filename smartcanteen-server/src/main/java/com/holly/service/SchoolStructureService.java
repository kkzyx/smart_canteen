package com.holly.service;

import com.holly.dto.SchoolTreeDTO;
import com.holly.entity.SchoolStructure;


import java.util.List;

public interface SchoolStructureService {
    List<SchoolTreeDTO> getTree();


    void saveOrUpdateNode(SchoolStructure node);

    void deleteNode(Long id);

    List<SchoolStructure> getByParentId(Long parentId);
}
