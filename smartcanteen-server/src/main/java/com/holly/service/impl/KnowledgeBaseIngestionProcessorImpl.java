package com.holly.service.impl;

import com.holly.entity.KnowledgeBase;
import com.holly.exception.BaseException;
import com.holly.mapper.KnowledgeBaseMapper;
import com.holly.service.FileService;
import com.holly.service.KnowledgeBaseIngestionProcessor;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.DocumentSplitter;

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
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KnowledgeBaseIngestionProcessorImpl implements KnowledgeBaseIngestionProcessor {

    private final FileService fileService;
    private final EmbeddingModel embeddingModel;
    private final ObjectProvider<EmbeddingStore<TextSegment>> embeddingStoreProvider;
    private final KnowledgeBaseMapper knowledgeBaseMapper;
    private final MinioClient minioClient;

    @Override
    public void ingest(String fileName, String bucketName, String objectName, String url) {
        EmbeddingStore<TextSegment> embeddingStore = embeddingStoreProvider.getIfAvailable();
        if (embeddingStore == null) {
            throw new BaseException("知识库功能不可用，请检查向量库配置");
        }

        KnowledgeBase existing = knowledgeBaseMapper.selectByFileName(fileName);
        if (existing != null) {
            log.warn("知识库文件已存在，跳过异步入库: {}", fileName);
            fileService.deleteFileMinio(bucketName, objectName);
            return;
        }

        Document document = loadDocument(fileName, bucketName, objectName);
        document.metadata().put("file_name", fileName);
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 100);
        MyEmbeddingStoreIngestor ingestor = MyEmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .documentSplitter(splitter)
                .build();

        try {
            MyIngestionResult ingestResult = ingestor.ingest(document);
            List<String> ids = ingestResult.getIds();
            String idsStr = StringUtils.join(ids, ",");

            KnowledgeBase knowledgeBase = KnowledgeBase.builder()
                    .fileName(fileName)
                    .vectorIds(idsStr)
                    .url(url)
                    .createdTime(new Date())
                    .build();

            knowledgeBaseMapper.insert(knowledgeBase);
            log.info("知识库异步入库成功: {}", fileName);
        } catch (Exception e) {
            log.error("知识库异步入库失败: {}", fileName, e);
            fileService.deleteFileMinio(bucketName, objectName);
            throw new BaseException(e.getMessage());
        }
    }

    private Document loadDocument(String fileName, String bucketName, String objectName) {
        if (bucketName == null || bucketName.isBlank() || objectName == null || objectName.isBlank()) {
            throw new BaseException("知识库文件地址缺失，bucketName/objectName 不能为空");
        }

        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build())) {
            return resolveParser(fileName).parse(inputStream);
        } catch (Exception e) {
            throw new BaseException("加载知识库文件失败: " + e.getMessage());
        }
    }

    private DocumentParser resolveParser(String fileName) {
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        if (".pdf".equals(suffix)) {
            return new ApachePdfBoxDocumentParser();
        }
        if (".txt".equals(suffix) || ".md".equals(suffix)) {
            return new TextDocumentParser();
        }
        if (".doc".equals(suffix) || ".docx".equals(suffix)
                || ".ppt".equals(suffix) || ".pptx".equals(suffix)
                || ".xls".equals(suffix) || ".xlsx".equals(suffix)) {
            return new ApachePoiDocumentParser();
        }
        return new ApacheTikaDocumentParser();
    }
}
