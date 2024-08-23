package com.web.resume.ai;

import com.google.cloud.NoCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.web.resume.ai.config.StorageType;
import com.web.resume.ai.impl.GoogleCloudStorageService;
import com.web.resume.ai.interfaces.StorageService;
import com.web.resume.ai.resources.CloudEventResource;
import io.aiven.testcontainers.fakegcsserver.FakeGcsServerContainer;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.tika.TikaParser;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static io.restassured.RestAssured.given;



@QuarkusTest
class CloudEventResourceTest {

    /*

    private static StorageService storageService;

    private static final FakeGcsServerContainer FAKE_GCS_SERVER_CONTAINER = new FakeGcsServerContainer();

    */


    @BeforeAll
    static void setup() {
        // FAKE_GCS_SERVER_CONTAINER.start();
        /*StorageService storageService = Mockito.mock(StorageService.class);
        Mockito.when(storageService.downloadFile(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> Files.readAllBytes(Paths.get("src/main/resources/test-file.pdf")));
        QuarkusMock.installMockForType(storageService, StorageService.class);*/
    }

    @Test
    void testCeEndpoint() throws IOException {

        /*final Storage storage = StorageOptions.newBuilder()
                .setCredentials(NoCredentials.getInstance())
                .setHost(FAKE_GCS_SERVER_CONTAINER.url())
                .setProjectId("test-project")
                .build()
                .getService();

        Bucket bucket = storage.get("test-bucket");
        if (bucket == null) {
            bucket = storage.create(BucketInfo.of("test-bucket"));
        }

        byte[] fileContent = Files.readAllBytes(Paths.get("src/main/resources/test-file.pdf"));


        bucket.create("example-file.pdf", fileContent);

        storageService = Mockito.mock(GoogleCloudStorageService.class);
        Mockito.when(storageService.downloadFile(Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String bucketName = invocation.getArgument(0);
                    String fileName = invocation.getArgument(1);
                    return storage.readAllBytes(bucketName, fileName);
                });

        byte[] file = storageService.downloadFile("test-bucket", "example-file.pdf");

        System.out.println("file is : " + Arrays.toString(file)); */


        /*String sampleJson = "{\"bucket\":\"test-bucket\",\"name\":\"example-file.pdf\"}";

       given()
                .contentType("application/json")
                .body(sampleJson)
                .when()
                .post("/ce")
                .then()
                .statusCode(400);*/
    }

}