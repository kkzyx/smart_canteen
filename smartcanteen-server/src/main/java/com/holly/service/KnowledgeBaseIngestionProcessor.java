package com.holly.service;

public interface KnowledgeBaseIngestionProcessor {

    void ingest(String fileName, String bucketName, String objectName, String url);
}
