package com.web.resume.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


@QuarkusTest
class CloudEventResourceTest {

    @Test
    void testCeEndpoint() {

        String sampleJson = "{\"key\":\"value\"}";

        given()
                .contentType("application/json")
                .body(sampleJson)
                .when()
                .post("/ce")
                .then()
                .statusCode(200);
    }

}