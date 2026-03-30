package com.holly.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.holly.entity.KnowledgeBase;
import com.holly.exception.BaseException;
import com.holly.exception.DishExecption;
import com.holly.mapper.KnowledgeBaseMapper;
import com.holly.query.KnowledgeBasePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.service.FileService;
import com.holly.service.KnowledgeBaseService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.MyEmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.MyIngestionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static com.holly.constant.MessageConstant.FILE_EXISTS;
import static com.holly.constant.MessageConstant.INVALID_PARAM;

@Service
@Slf4j
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

    @Autowired
    private FileService fileService;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired(required = false)
    private EmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;

    private static final String OK = "ok";
    private static final String ERROR = "error";

    @Override
    public String addKnowledgeBase(MultipartFile file) throws Exception {
        if (embeddingStore == null) {
            throw new BaseException("知识库功能不可用：请检查 Zilliz Cloud / Milvus 向量库配置。");
        }

        String originalFilename = file.getOriginalFilename();
        log.info("原始名称==> {}", originalFilename);
        KnowledgeBase dbKnowledgeBase = knowledgeBaseMapper.selectByFileName(originalFilename);
        if (dbKnowledgeBase != null) {
            throw new DishExecption(FILE_EXISTS);
        }

        String url = fileService.uploadMinio(file);
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        log.info("文件后缀==> {}", suffix);

        Document document;
        if (".pdf".equals(suffix)) {
            document = UrlDocumentLoader.load(url, new ApachePdfBoxDocumentParser());
        } else if (".txt".equals(suffix) || ".md".equals(suffix)) {
            document = UrlDocumentLoader.load(url, new TextDocumentParser());
        } else if (".doc".equals(suffix) || ".docx".equals(suffix)
                || ".ppt".equals(suffix) || ".pptx".equals(suffix)
                || ".xls".equals(suffix) || ".xlsx".equals(suffix)) {
            document = UrlDocumentLoader.load(url, new ApachePoiDocumentParser());
        } else {
            document = UrlDocumentLoader.load(url, new ApacheTikaDocumentParser());
        }

        document.metadata().put("file_name", originalFilename);
        DocumentSplitter recursive = DocumentSplitters.recursive(300, 100);

        MyEmbeddingStoreIngestor ingestor = MyEmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(recursive)
                .build();

        try {
            MyIngestionResult ingest = ingestor.ingest(document);
            List<String> ids = ingest.getIds();
            String idsStr = StringUtils.join(ids, ",");

            KnowledgeBase knowledgeBase = KnowledgeBase.builder()
                    .fileName(originalFilename)
                    .vectorIds(idsStr)
                    .url(url)
                    .createdTime(new Date())
                    .build();

            int insert = knowledgeBaseMapper.insert(knowledgeBase);
            if (insert > 0) {
                log.info("新增知识库成功");
                return OK;
            }
        } catch (Exception e) {
            log.error("向量存储失败", e);
            fileService.deleteFileMinio(url);
            throw new BaseException(e.getMessage());
        }

        log.error("新增知识库失败");
        return ERROR;
    }

    @Override
    public PageResult page(KnowledgeBasePageQueryDTO dto) {
        if (dto == null) {
            throw new BaseException(INVALID_PARAM);
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
            String vectorIds = knowledgeBase.getVectorIds();
            deleteVectorIds(vectorIds);
            fileService.deleteFileMinio(knowledgeBase.getUrl());
        }

        int delete = knowledgeBaseMapper.deleteByIds(ids);
        if (delete > 0) {
            log.info("删除知识库成功");
            return delete;
        }

        log.error("删除知识库失败");
        return 0;
    }

    private void deleteVectorIds(String vectorIds) {
        if (embeddingStore == null) {
            log.warn("EmbeddingStore 不可用，跳过删除向量数据");
            return;
        }

        String[] split = vectorIds.split(",");
        for (String id : split) {
            embeddingStore.remove(id);
        }
    }
}
