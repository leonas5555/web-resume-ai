package com.web.resume.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.resume.ai.resources.CloudEventResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.MockitoConfig;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@QuarkusTest
class CloudEventResourceTest {

    @Inject
    CloudEventResource cloudEventResource;

    @InjectMock
    @MockitoConfig(convertScopes = true)
    EventBus eventBus;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testReceiveMessage_Success() throws Exception {
        // Arrange
        JsonNode mockCloudEventJson = objectMapper.readTree("{\"event\": \"file-uploaded\", \"data\": \"test\"}");

        Message<String> mockMessage = mock(Message.class);

        when(mockMessage.body()).thenReturn("File processed successfully");
        // Mock EventBus response
        when(eventBus.<String>request(any(), any())).thenReturn(Uni.createFrom().item(mockMessage));

        // Act
        Uni<Response> result = cloudEventResource.receiveMessage(mockCloudEventJson);

        // Assert
        Response response = result.await().indefinitely();
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("File processed successfully", response.getEntity());
    }

    @Test
    public void testReceiveMessage_Failure() throws Exception {
        // Arrange
        JsonNode mockCloudEventJson = objectMapper.readTree("{\"event\": \"file-uploaded\", \"data\": \"test\"}");

        // Mock EventBus failure
        when(eventBus.<String>request(any(), any()))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Processing failed")));

        // Act
        Uni<Response> result = cloudEventResource.receiveMessage(mockCloudEventJson);

        // Assert
        Response response = result.await().indefinitely();
        assertNotNull(response);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Processing failed"));
    }

}