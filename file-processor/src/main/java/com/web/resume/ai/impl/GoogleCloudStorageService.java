package com.web.resume.ai.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.web.resume.ai.config.StorageType;
import com.web.resume.ai.interfaces.StorageService;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@StorageType("gcs")
public class GoogleCloudStorageService implements StorageService {

    @Inject
    Storage storage;
    @Inject
    Vertx vertx;

    public GoogleCloudStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public Uni<byte[]> downloadFile(String bucketName, String fileName) {
        // Using Vertx's executeBlocking to perform blocking I/O in a non-blocking way
        return vertx.executeBlocking(() -> {
            // This is the blocking operation
            Blob blob = storage.get(BlobId.of(bucketName, fileName));
            if (blob == null) {
                throw new RuntimeException("File not found in bucket: " + bucketName + ", filename: " + fileName);
            }
            return blob.getContent();
        });
    }
}
