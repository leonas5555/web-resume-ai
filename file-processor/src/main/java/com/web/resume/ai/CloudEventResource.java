package com.web.resume.ai;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Path("/ce")
public class CloudEventResource {

    private static final Logger LOGGER = Logger.getLogger(CloudEventResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response receiveMessage(String cloudEventJson) {

        LOGGER.info("Received CloudEvent: " + cloudEventJson);


        return Response.ok().build();
    }
}
