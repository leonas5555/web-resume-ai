package com.web.resume.ai.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.web.resume.ai.interfaces.StorageService;
import com.web.resume.ai.util.JsonValidationUtil;
import com.web.resume.ai.util.PdfParserUtil;
import io.quarkus.tika.TikaParser;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;


import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Path("/ce")
public class CloudEventResource {

    @Inject
    StorageService storageService;
    @Inject
    TikaParser parser;
    @Inject
    PdfParserUtil pdfParserUtil;

    private static final Logger LOGGER = Logger.getLogger(CloudEventResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> receiveMessage(JsonNode cloudEventJson) {

        LOGGER.info("Received CloudEvent: " + cloudEventJson);
        LOGGER.info("StorageService is " + (storageService == null ? "null" : "initialized"));

        return JsonValidationUtil.validateStorageCloudEvent(cloudEventJson)
                .onItem().transformToUni(ignored -> {
                    // Extract bucketName and fileName after validation
                    String bucketName = cloudEventJson.get("bucket").asText();
                    String fileName = cloudEventJson.get("name").asText();

                    // Check if the file is a PDF
                    if (fileName.endsWith(".pdf")) {
                        // Call the processPdfFile method which returns a Uni<String> with the extracted text
                        return processPdfFile(bucketName, fileName)
                                .onItem().transform(extractedText -> {
                                    LOGGER.info("Successfully processed PDF and extracted text.");
                                    // Return the extracted text as part of the response
                                    return Response.ok().entity(extractedText).build();
                                });
                    } else {
                        // If not a PDF, just return OK to commit consumed message.
                        return Uni.createFrom().item(Response.ok().build());
                    }
                })
                .onFailure().recoverWithItem(throwable -> {
                    LOGGER.error("Error processing the file", throwable);
                    // Return an internal server error response if something fails
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error in processing").build();
                });
    }
    private Uni<String> processPdfFile(String bucketName, String fileName) {
        return storageService.downloadFile(bucketName, fileName)
                .onItem().transformToUni(fileContent -> {
                    // Create an InputStream from the downloaded byte array
                    try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
                        // Extract the text asynchronously using extractTextAsync
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
