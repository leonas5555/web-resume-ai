package com.web.resume.ai;


import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @Inject
    EmbeddingModel model;
    @Inject
    Neo4jEmbeddingStore store;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        TextSegment text = TextSegment.from("Hello, how are you?");

        Response<Embedding> response = model.embed("Hello, how are you?");

        store.add(response.content(), text);

        System.out.println(response);

        return "Hello from Quarkus REST";
    }
}
