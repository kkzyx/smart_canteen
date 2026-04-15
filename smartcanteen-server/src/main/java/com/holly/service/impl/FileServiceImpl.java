package com.holly.service.impl;

import com.holly.model.MinioUploadResult;
import com.holly.properties.MinioProperties;
import com.holly.service.FileService;
import com.holly.utils.AliOssUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final AliOssUtil aliOssUtil;
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String uploadAliOss(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String filename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String date = new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
        String objectName = date + filename + "-" + UUID.randomUUID() + extension;

        log.info("上传到 OSS 的文件名: {}", objectName);
        String filePath = null;
        try {
            filePath = aliOssUtil.upload(file.getBytes(), objectName);
            log.info("上传 OSS 成功: {}", filePath);
        } catch (IOException e) {
            log.error("上传 OSS 失败", e);
        }
        return filePath;
    }

    @Override
    public MinioUploadResult uploadMinio(MultipartFile file) throws ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        String bucketName = minioProperties.getBucketName();
        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            String policyConfig = createBucketPolicyConfig(bucketName);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(bucketName).config(policyConfig).build()
            );
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String objectName = new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                + "/" + UUID.randomUUID() + extension;

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        String url = String.join("/", minioProperties.getEndpoint(), bucketName, objectName);
        log.info("上传文件到 MinIO 成功: {}", url);
        return MinioUploadResult.builder()
                .bucketName(bucketName)
                .objectName(objectName)
                .url(url)
                .build();
    }

    @Override
    public void deleteFileMinio(String url) {
        try {
            if (url == null || url.isBlank()) {
                return;
            }
            String bucketName = minioProperties.getBucketName();
            int bucketIndex = url.indexOf(bucketName);
            if (bucketIndex != -1) {
                String objectPath = url.substring(bucketIndex + bucketName.length() + 1);
                deleteFileMinio(bucketName, objectPath);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFileMinio(String bucketName, String objectName) {
        try {
            if (bucketName == null || bucketName.isBlank() || objectName == null || objectName.isBlank()) {
                return;
            }
            log.info("删除 MinIO 文件: {}/{}", bucketName, objectName);
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFileAliOss(String url) {
        try {
            log.info("删除阿里云文件: {}", url);
            aliOssUtil.deleteFile(url);
        } catch (Exception e) {
            log.error("删除阿里云文件失败: {}", url, e);
            throw new RuntimeException(e);
        }
    }

    private String createBucketPolicyConfig(String bucketName) {
        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }
}
