package com.web.resume.ai.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.resume.ai.interfaces.StorageService;
import io.quarkus.tika.TikaParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

@Path("/ce")
public class CloudEventResource {

    @Inject
    StorageService storageService;
    @Inject
    TikaParser parser;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = Logger.getLogger(CloudEventResource.class.getName());


    @POST
    @Path("/text")
    @Consumes({ "application/pdf", "application/vnd.oasis.opendocument.text" })
    @Produces(MediaType.TEXT_PLAIN)
    public String extractText(InputStream stream) {
        Instant start = Instant.now();

        String text = parser.getText(stream);

        Instant finish = Instant.now();

        LOGGER.info(Duration.between(start, finish).toMillis() + " mls have passed");

        return text;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receiveMessage(String cloudEventJson) {

        LOGGER.info("Received CloudEvent: " + cloudEventJson);
        LOGGER.info("StorageService is " + (storageService == null ? "null" : "initialized"));

        try {
            JsonNode rootNode = objectMapper.readTree(cloudEventJson);

            JsonNode bucketNode = rootNode.get("bucket");
            JsonNode nameNode = rootNode.get("name");

            if (bucketNode == null || nameNode == null) {
                LOGGER.log(Level.ERROR, "Missing required fields in the JSON: bucket or name");
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields: bucket or name").build();
            }

            String bucketName = bucketNode.asText();
            String fileName = nameNode.asText();

            if (bucketName.isEmpty() || fileName.isEmpty()) {
                LOGGER.log(Level.ERROR, "Requered fields are empty");
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing required fields: bucket or name").build();
            }

            if (fileName.endsWith(".pdf")) {
                processPdfFile(bucketName, fileName);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error processing a file", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error in processing").build();
        }

        return Response.ok().build();
    }
    private void processPdfFile(String bucketName, String fileName) {
        try {
            byte[] fileContent = storageService.downloadFile(bucketName, fileName);
            try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {

                String text = parser.getText(inputStream);

                LOGGER.info("Extracted text: " + text);
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, "Error processing pdf file", e);
            throw new RuntimeException(e);
        }
    }
}
