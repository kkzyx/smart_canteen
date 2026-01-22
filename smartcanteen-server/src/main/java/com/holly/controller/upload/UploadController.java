package com.holly.controller.upload;

import com.holly.constant.MessageConstant;
import com.holly.exception.FileException;
import com.holly.result.Result;
import com.holly.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.holly.constant.MessageConstant.FILE_NOT_SELECTED;

/**
 * @description
 */
@Slf4j
@Tag(name = "上传文件接口")
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {
    private final FileService fileService;

    /**
     * 文件上传阿里云版
     * @param file
     * @return
     */
    @PostMapping
    @Operation(summary = "文件上传")
    public Result<String> uploadAliOss(@RequestPart("file") MultipartFile file) {
        log.info("前端传过来的文件==> {}", file);
        if (file.isEmpty()) {
            throw new FileException(FILE_NOT_SELECTED);
        }
        // 上传文件到OSS，成功之后返回文件的路径
        try {
            String filePath = fileService.uploadAliOss(file);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败，原因==> {}", e.getMessage());
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

    /**
     * 文件上传Minio版
     *
     * @param file
     * @return
     */
    /*@PostMapping
    @Operation(summary = "文件上传")
    public Result<String> uploadMinio(@RequestPart("file") MultipartFile file) {
        log.info("前端传过来的文件==> {}", file);
        if (file.isEmpty()) {
            throw new FileException(FILE_NOT_SELECTED);
        }
        // 上传文件到Minio，成功之后返回文件的路径
        try {
            String filePath = fileService.uploadMinio(file);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败，原因==> {}", e.getMessage());
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }*/
}
