package com.web.resume.ai.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.web.resume.ai.config.StorageType;
import com.web.resume.ai.interfaces.StorageService;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;

@ApplicationScoped
@StorageType("s3")
public class AwsS3StorageService implements StorageService {

    private final AmazonS3 s3Client;

    public AwsS3StorageService() {
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        try (S3ObjectInputStream s3is = s3Client.getObject(bucketName, fileName).getObjectContent()) {
            return s3is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read S3 object content", e);
        }
    }
}
