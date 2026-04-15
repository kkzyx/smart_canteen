package com.holly.service;

import com.holly.model.MinioUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadAliOss(MultipartFile file) throws IOException;

    MinioUploadResult uploadMinio(MultipartFile file) throws Exception;

    void deleteFileMinio(String url);

    void deleteFileMinio(String bucketName, String objectName);

    void deleteFileAliOss(String url);
}
