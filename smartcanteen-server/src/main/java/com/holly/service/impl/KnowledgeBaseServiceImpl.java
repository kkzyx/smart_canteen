package com.holly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.holly.entity.KnowledgeBase;
import com.holly.exception.DishExecption;
import com.holly.mapper.KnowledgeBaseMapper;
import com.holly.model.MinioUploadResult;
import com.holly.mq.message.KnowledgeBaseIngestMessage;
import com.holly.mq.producer.BusinessEventProducer;
import com.holly.query.KnowledgeBasePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.service.FileService;
import com.holly.service.KnowledgeBaseService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.holly.constant.MessageConstant.FILE_EXISTS;
import static com.holly.constant.MessageConstant.INVALID_PARAM;

@Service
@Slf4j
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    @Autowired
    private FileService fileService;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    @Autowired
    private BusinessEventProducer businessEventProducer;

    @Autowired
    private ObjectProvider<EmbeddingStore<TextSegment>> embeddingStoreProvider;

    @Override
    public String addKnowledgeBase(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        log.info("knowledge source file: {}", originalFilename);

        KnowledgeBase dbKnowledgeBase = knowledgeBaseMapper.selectByFileName(originalFilename);
        if (dbKnowledgeBase != null) {
            throw new DishExecption(FILE_EXISTS);
        }

        MinioUploadResult uploadResult = fileService.uploadMinio(file);
        businessEventProducer.sendKnowledgeIngestMessageAfterCommit(KnowledgeBaseIngestMessage.builder()
                .fileName(originalFilename)
                .bucketName(uploadResult.getBucketName())
                .objectName(uploadResult.getObjectName())
                .url(uploadResult.getUrl())
                .build());
        log.info("knowledge file sent to RabbitMQ: {}", originalFilename);
        return "accepted";
    }

    @Override
    public PageResult page(KnowledgeBasePageQueryDTO dto) {
        if (dto == null) {
            throw new DishExecption(INVALID_PARAM);
        }

        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        Page<KnowledgeBase> knowledgeBases = knowledgeBaseMapper.pageQuery(dto);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(knowledgeBases.getTotal());
        pageResult.setRecords(knowledgeBases);
        return pageResult;
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new DishExecption(INVALID_PARAM);
        }

        List<KnowledgeBase> knowledgeBaseList = knowledgeBaseMapper.selectByIdList(ids);
        for (KnowledgeBase knowledgeBase : knowledgeBaseList) {
            if (knowledgeBase.getVectorIds() != null && !knowledgeBase.getVectorIds().isBlank()) {
                deleteVectorIds(knowledgeBase.getVectorIds());
            }
            fileService.deleteFileMinio(knowledgeBase.getUrl());
        }

        int delete = knowledgeBaseMapper.deleteByIds(ids);
        if (delete > 0) {
            log.info("delete knowledge base success");
            return delete;
        }

        log.error("delete knowledge base failed");
        return 0;
    }

    private void deleteVectorIds(String vectorIds) {
        EmbeddingStore<TextSegment> embeddingStore = embeddingStoreProvider.getIfAvailable();
        if (embeddingStore == null) {
            log.warn("embedding store unavailable, skip vector cleanup");
            return;
        }

        String[] split = vectorIds.split(",");
        for (String id : split) {
            embeddingStore.remove(id);
        }
    }
}
