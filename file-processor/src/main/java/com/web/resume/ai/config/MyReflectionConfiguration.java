package com.web.resume.ai.config;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.pdfbox.pdmodel.PDDocument;

@RegisterForReflection(targets={
        PDDocument.class,})
public class MyReflectionConfiguration {
}