package com.web.resume.ai.impl;

import com.web.resume.ai.config.StorageType;
import com.web.resume.ai.interfaces.StorageService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
@StorageType("s3")
public class AwsS3StorageService implements StorageService {


    @Inject
    S3AsyncClient s3AsyncClient;

    public AwsS3StorageService() {
        this.s3AsyncClient = S3AsyncClient.builder().build();
    }

    @Override
    public Uni<byte[]> downloadFile(String bucketName, String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        CompletableFuture<byte[]> future = s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                .thenApply(BytesWrapper::asByteArray);

        // Convert CompletableFuture to Uni for non-blocking operation
        return Uni.createFrom().completionStage(future);
    }
}
