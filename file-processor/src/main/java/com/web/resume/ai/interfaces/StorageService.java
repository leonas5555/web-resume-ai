package com.web.resume.ai.interfaces;

import io.smallrye.mutiny.Uni;

public interface StorageService {
    Uni<byte[]> downloadFile(String bucketName, String fileName);
}
