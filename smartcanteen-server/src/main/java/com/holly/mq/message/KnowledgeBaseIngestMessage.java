package com.holly.mq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBaseIngestMessage {

    private String fileName;

    private String bucketName;

    private String objectName;

    private String url;
}
