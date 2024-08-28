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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        String text = "Hello, how are you?";

        if (documentsRepository.documentExistsByText(text)) {
            LOGGER.info( "Document exists. skip embedding");
        } else {
            TextSegment textSegment = TextSegment.from("Hello, how are you?");
            Response<Embedding> response = model.embed(textSegment);
            store.add(response.content(), textSegment);
        }

        Document document = documentsRepository.getDocumentByText(text);

        LOGGER.info("Document: " + document);

        return "Text embedding done";
    }
}
