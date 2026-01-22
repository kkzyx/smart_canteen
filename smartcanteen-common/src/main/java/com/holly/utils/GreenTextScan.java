package com.holly.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationPlusRequest;
import com.aliyun.green20220302.models.TextModerationPlusResponse;
import com.aliyun.green20220302.models.TextModerationPlusResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "training.aliyun")
public class GreenTextScan {

    private String accessKeyId;
    private String secret;

    public Map<String, String> greenTextScan(String content) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(secret);
        config.setRegionId("cn-shanghai");
        config.setEndpoint("green.cn-shanghai.aliyuncs.com");
        config.setReadTimeout(6000);
        config.setConnectTimeout(3000);

        Client client = new Client(config);

        JSONObject serviceParameters = new JSONObject();
        serviceParameters.put("content", content);

        TextModerationPlusRequest request = new TextModerationPlusRequest();
        request.setService("comment_detection_pro");
        request.setServiceParameters(serviceParameters.toJSONString());

        Map<String, String> resultMap = new HashMap<>();

        try {
            TextModerationPlusResponse response = client.textModerationPlus(request);
            if (response.getStatusCode() == 200) {
                TextModerationPlusResponseBody body = response.getBody();
                if (body != null && body.getCode() == 200) {
                    var data = body.getData();
                    if (data != null && data.getResult() != null) {
                        data.getResult().forEach(result -> {
                            resultMap.put("label", result.getLabel());
                            resultMap.put("suggestion", result.getDescription());
                        });
                        return resultMap;
                    }
                }
            }
            resultMap.put("suggestion", "pass");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
