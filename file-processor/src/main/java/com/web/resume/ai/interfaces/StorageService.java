package com.web.resume.ai.interfaces;

public interface StorageService {
    byte[] downloadFile(String bucketName, String fileName);
}
