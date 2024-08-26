package com.web.resume.ai.util;

import com.fasterxml.jackson.databind.JsonNode;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.jboss.logging.Logger;

import java.util.Map;

public class JsonValidationUtil {

    private static final Logger LOGGER = Logger.getLogger(JsonValidationUtil.class.getName());

    /**
     * Validates that the given JsonNode contains all the required fields as specified by the `requiredFields`.
     * @param jsonNode The JsonNode to be validated.
     * @param requiredFields A map where the key is the field name to check and the value is an error message if the field is missing or empty.
     * @return Uni<Void> A reactive Uni representing success or failure of the validation.
     */
    public static Uni<Void> validateFields(JsonNode jsonNode, Map<String, String> requiredFields) {
        return Uni.createFrom().item(Unchecked.supplier(() -> {
            for (Map.Entry<String, String> entry : requiredFields.entrySet()) {
                String fieldName = entry.getKey();
                String errorMessage = entry.getValue();

                JsonNode fieldNode = jsonNode.get(fieldName);

                if (fieldNode == null || fieldNode.asText().isEmpty()) {
                    LOGGER.error(errorMessage);
                    throw new IllegalArgumentException(errorMessage);
                }
            }

            return null;  // Success, no errors
        }));
    }

    /**
     * Example: Validate cloud event JSON for specific fields like 'bucket' and 'name'.
     * @param cloudEventJson The cloud event JsonNode.
     * @return Uni<Void> A reactive Uni representing success or failure of the validation.
     */
    public static Uni<Void> validateStorageCloudEvent(JsonNode cloudEventJson) {
        Map<String, String> requiredFields = Map.of(
                "bucket", "Missing or empty required field: bucket",
                "name", "Missing or empty required field: name"
        );

        return validateFields(cloudEventJson, requiredFields);
    }
}
