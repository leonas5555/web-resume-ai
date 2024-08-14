package com.web.resume.ai.exceptions;

public class StorageServiceNotFoundException extends RuntimeException {
    public StorageServiceNotFoundException(String storageServiceType) {
        super("No StorageService implementation found for storage type: " + storageServiceType);
    }
}
