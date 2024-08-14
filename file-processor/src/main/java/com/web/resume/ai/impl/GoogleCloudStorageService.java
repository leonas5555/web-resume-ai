package com.web.resume.ai.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.web.resume.ai.config.StorageType;
import com.web.resume.ai.interfaces.StorageService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@StorageType("gcs")
public class GoogleCloudStorageService implements StorageService {

    private final Storage storage;

    public GoogleCloudStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        Blob blob = storage.get(BlobId.of(bucketName, fileName));
        if (blob == null) {
            throw new IllegalArgumentException("File not found in bucket");
        }
        return blob.getContent();
    }
}
