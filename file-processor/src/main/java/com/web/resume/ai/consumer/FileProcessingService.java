package com.web.resume.ai.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.web.resume.ai.interfaces.StorageService;
import com.web.resume.ai.util.JsonValidationUtil;
import com.web.resume.ai.util.PdfParserUtil;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@ApplicationScoped
public class FileProcessingService {

    private static final Logger LOGGER = Logger.getLogger(FileProcessingService.class);

    @Inject
    StorageService storageService;

    @Inject
    PdfParserUtil pdfParserUtil;

    @ConsumeEvent("file-processing")  // This will now be asynchronous by returning a Uni
    public Uni<String> processCloudEvent(JsonNode cloudEventJson) {
        LOGGER.info("Received CloudEvent for processing: " + cloudEventJson);

        // The validateStorageCloudEvent should also return a Uni to make it asynchronous
        return JsonValidationUtil.validateStorageCloudEvent(cloudEventJson)
                .onItem().transformToUni(ignored -> {
                    String bucketName = cloudEventJson.get("bucket").asText();
                    String fileName = cloudEventJson.get("name").asText();

                    LOGGER.info("Processing file: " + fileName + " from bucket: " + bucketName);

                    // Process the file asynchronously (only PDFs for now)
                    if (fileName.endsWith(".pdf")) {
                        return processPdfFile(bucketName, fileName)
                                .onItem().invoke(extractedText -> LOGGER.info("Successfully processed PDF and extracted text."))
                                .onFailure().invoke(throwable -> LOGGER.error("Error processing file: " + fileName, throwable));
                    } else {
                        LOGGER.info("File is not a PDF. Skipping.");
                        return Uni.createFrom().item("File is not a PDF. No processing done.");
                    }
                });
    }

    private Uni<String> processPdfFile(String bucketName, String fileName) {
        // Asynchronously download and process the file
        return storageService.downloadFile(bucketName, fileName)
                .onItem().transformToUni(fileContent -> {
                    try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
                        // Asynchronously extract text from the PDF
                        return pdfParserUtil.extractTextAsync(inputStream)
                                .onItem().invoke(text -> LOGGER.info("Extracted text: " + text))
                                .onFailure().invoke(e -> LOGGER.error("Error processing PDF file", e));
                    } catch (Exception e) {
                        LOGGER.error("Error creating InputStream from file content", e);
                        return Uni.createFrom().failure(new RuntimeException("Error creating InputStream from file content", e));
                    }
                });
    }
}
