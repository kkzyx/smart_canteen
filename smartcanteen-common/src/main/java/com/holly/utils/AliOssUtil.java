package com.holly.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * @description
 */
@Data
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AliOssUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传
     *
     * @param bytes    文件字节数组
     * @param fileName 文件名
     */
    public String upload(byte[] bytes, String fileName) {
        // 创建OSSClient实例
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求
            oss.putObject(bucketName, fileName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            System.out.println(
                    "Caught an OSSException, which means your request made it to OSS, but was rejected with an error " +
                            "response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println(
                    "Caught an ClientException, which means the client encountered a serious internal problem while trying "
                            + "to communicate with OSS, such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (oss != null) {
                oss.shutdown();
            }
        }

        // 文件访问路径规则 https://help.aliyun.com/zh/oss/user-guide/oss-domain-names
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder.append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(fileName);
        log.info("文件上传到==> {}", stringBuilder.toString());
        return stringBuilder.toString();
    }


    public void deleteFile(String url) {
        // 创建OSSClient实例
        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        // https://springboot-sky-itcast-take-out.oss-cn-beijing.aliyuncs.com/2025/09/21/10-01-af09538c-cd39-4507-81c1-ed9e36bf567d.png
        String objectName = url.substring(url.indexOf("/", url.indexOf(bucketName + "." + endpoint)) + 1);
        //截取后的结果：2025/09/21/10-01-af09538c-cd39-4507-81c1-ed9e36bf567d.png
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            oss.deleteObject(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (oss != null) {
                oss.shutdown();
            }
        }
    }
}
