package com.web.resume.ai.config;

import com.web.resume.ai.exceptions.StorageServiceNotFoundException;
import com.web.resume.ai.interfaces.StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class StorageServiceProducer {

    @Inject
    @StorageType("gcs")
    Instance<StorageService> gcsStorageService;

    @Inject
    @StorageType("s3")
    Instance<StorageService> s3StorageService;

    @ConfigProperty(name = "storage.service.type")
    String storageServiceType;

    @Produces
    public StorageService produceStorageService() {
        return switch (storageServiceType) {
            case "gcs" -> gcsStorageService.get();
            case "s3" -> s3StorageService.get();
            default -> throw new StorageServiceNotFoundException(storageServiceType);
        };
    }
    /*@Produces
    public StorageService produceStorageService() {
        try {
            Instance<StorageService> instance = CDI.current().select(StorageService.class, new StorageTypeLiteral(storageServiceType));
            if (instance.isUnsatisfied()) {
                throw new StorageServiceNotFoundException(storageServiceType);
            }
            return instance.get();
        } catch (Exception e) {
            throw new StorageServiceNotFoundException(storageServiceType);
        }
    }*/
}
