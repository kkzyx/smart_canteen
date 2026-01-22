package com.holly.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    /**
     * 文件上传阿里云版
     *
     * @param file
     * @return
     * @throws IOException
     */
    String uploadAliOss(MultipartFile file) throws IOException;

    /**
     * 文件上传Minio版
     *
     * @param file
     * @return
     * @throws Exception
     */
    String uploadMinio(MultipartFile file) throws Exception;

    /**
     * 文件删除Minio版
     * @param url
     */
    void deleteFileMinio(String url);

    /**
     * 文件删除阿里云版
     * @param url
     */
    void deleteFileAliOss(String url);
}
