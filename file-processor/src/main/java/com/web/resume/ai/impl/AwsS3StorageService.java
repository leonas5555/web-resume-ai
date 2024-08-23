package com.web.resume.ai.impl;

import com.web.resume.ai.config.StorageType;
import com.web.resume.ai.interfaces.StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@ApplicationScoped
@StorageType("s3")
public class AwsS3StorageService implements StorageService {


    @Inject
    S3Client s3Client;

    public AwsS3StorageService() {
        this.s3Client = S3Client.builder().build();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(getObjectRequest)) {
            return s3is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read S3 object content", e);
        }
    }
}
