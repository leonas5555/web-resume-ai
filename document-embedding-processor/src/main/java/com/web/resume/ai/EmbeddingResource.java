package com.web.resume.ai;


import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.neo4j.Neo4jEmbeddingStore;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Path("/embedding")
public class EmbeddingResource {

    private static final Logger LOGGER = Logger.getLogger(EmbeddingResource.class);

    @Inject
    EmbeddingModel model;
    @Inject
    Neo4jEmbeddingStore store;
    @Inject
    DocumentsRepository documentsRepository;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String embedText(String text) {

        if (documentsRepository.documentExistsByText(text)) {
            LOGGER.info( "Document exists. skip embedding");
        } else {
            TextSegment textSegment = TextSegment.from(text);
            LOGGER.info( "Sending text to embedding model");
            Response<Embedding> response = model.embed(textSegment);
            LOGGER.info( "Embedding vector received");
            LOGGER.info( "Storing embedding vector to database");
            store.add(response.content(), textSegment);
        }
        return "Text embedding done";
    }
}
