package com.holly.service.impl;

import com.holly.entity.KnowledgeBase;
import com.holly.exception.BaseException;
import com.holly.exception.DishExecption;
import com.holly.mapper.KnowledgeBaseMapper;
import com.holly.query.KnowledgeBasePageQueryDTO;
import com.holly.result.PageResult;
import com.holly.service.FileService;
import com.holly.service.KnowledgeBaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.tika.ApacheTikaDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
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
    private RedisEmbeddingStore redisEmbeddingStore;
    
    @Autowired
    private KnowledgeBaseMapper knowledgeBaseMapper;
    private static final String OK = "ok";
    private static final String ERROR = "error";

    /**
     * 新增知识库
     *
     * @param file
     * @return
     */
    @Override
    public String addKnowledgeBase(MultipartFile file) throws Exception {
        // 检查 RedisEmbeddingStore 是否可用
        if (redisEmbeddingStore == null) {
            throw new BaseException("知识库功能不可用：需要 Redis Stack 支持。请启动 Redis Stack 或联系管理员。");
        }
        
        //获取原始名称
        String originalFilename = file.getOriginalFilename();
        log.info("原始名称==> {}", originalFilename);
        KnowledgeBase dbKnowledgeBase = knowledgeBaseMapper.selectByFileName(originalFilename);
        //判断文件是否存在
        if (dbKnowledgeBase != null){
            throw new DishExecption(FILE_EXISTS);
        }
        //先把文件上传到minio
        String url = fileService.uploadMinio(file);
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("文件后缀==> {}", suffix);
        Document document = null;
        //判断文件类型 使用不同的解析器
        if (".pdf".equals(suffix)) {
            document = UrlDocumentLoader.load(url, new ApachePdfBoxDocumentParser());
        } else if (".txt".equals(suffix)) {
            document = UrlDocumentLoader.load(url, new TextDocumentParser());
        } else if(".md".equals(suffix)){
            document = UrlDocumentLoader.load(url, new TextDocumentParser());
        } else {
            //使用通用类型解析
            document = UrlDocumentLoader.load(url, new ApacheTikaDocumentParser());
        }

        document.metadata().put("file_name", originalFilename);
        //构建通用文本分割器
        DocumentSplitter recursive = DocumentSplitters.recursive(300, 100);

        //构建文本向量存储器
        MyEmbeddingStoreIngestor ingestor = MyEmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(redisEmbeddingStore)
                .documentSplitter(recursive)
                .build();
        //向量存储
        MyIngestionResult ingest = null;
        try {
            ingest = ingestor.ingest(document);
            //获取存储在redis中的id
            List<String> ids = ingest.getIds();
            //以,进行拼接
            String idsStr = StringUtils.join(ids, ",");

            KnowledgeBase knowledgeBase = KnowledgeBase.builder()
                    .fileName(originalFilename)
                    .redisIds(idsStr)
                    .url(url)
                    .createdTime(new Date())
                    .build();
            int insert = knowledgeBaseMapper.insert(knowledgeBase);

            if (insert > 0) {
                log.info("新增知识库成功");
                return OK;
            }
        } catch (Exception e) {
            log.error("向量存储失败");
            e.printStackTrace();
            //清理minio 文件
            fileService.deleteFileMinio(url);
            throw new BaseException(e.getMessage());
        }
        log.error("新增知识库失败");
        return ERROR;
    }

    /**
     * 分页查询知识库
     *
     * @param dto
     * @return
     */
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

    /**
     * 批量除知识库
     *
     * @param ids
     */
    @Override
    public int deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new DishExecption(INVALID_PARAM);
        }

        List<KnowledgeBase> knowledgeBaseList = knowledgeBaseMapper.selectByIdList(ids);
        for (KnowledgeBase knowledgeBase : knowledgeBaseList) {
            //获取redis中的id 以,分隔
            String redisIds = knowledgeBase.getRedisIds();
            deleteRedisIds(redisIds);
            //删除minio中的文件
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

    /**
     * 删除redis中的向量数据
     *
     * @param redisIds
     */
    private void deleteRedisIds(String redisIds) {
        // 检查 RedisEmbeddingStore 是否可用
        if (redisEmbeddingStore == null) {
            log.warn("RedisEmbeddingStore 不可用，跳过删除向量数据");
            return;
        }
        
        //获取redis中的id 以,分隔
        String[] split = redisIds.split(",");
        for (String id : split) {
            redisEmbeddingStore.remove(id);
        }
    }
}
