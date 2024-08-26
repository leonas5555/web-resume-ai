package com.web.resume.ai.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.web.resume.ai.interfaces.StorageService;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/ce")
public class CloudEventResource {

    @Inject
    StorageService storageService;
    @Inject
    EventBus bus;

    private static final Logger LOGGER = Logger.getLogger(CloudEventResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> receiveMessage(JsonNode cloudEventJson) {

        LOGGER.info("Received CloudEvent: " + cloudEventJson);
        LOGGER.info("StorageService is " + (storageService == null ? "null" : "initialized"));

        // Send the event to the EventBus asynchronously
        return bus.<String>request("file-processing", cloudEventJson)
                .onItem().transform(response -> {
                    LOGGER.info("Processing result: " + response.body());
                    return Response.ok(response.body()).build();
                })
                .onFailure().recoverWithItem(throwable -> {
                    // TODO could introduce later more delegated response status handling
                    LOGGER.error("Error processing the CloudEvent", throwable);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error in processing cloud event: " + throwable.getMessage()).build();
                });
    }
}
