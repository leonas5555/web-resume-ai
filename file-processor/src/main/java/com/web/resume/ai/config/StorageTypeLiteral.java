package com.web.resume.ai.config;


import jakarta.enterprise.util.AnnotationLiteral;

public class StorageTypeLiteral extends AnnotationLiteral<StorageType> implements StorageType {

    private final String value;

    public StorageTypeLiteral(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}

