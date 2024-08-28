package com.web.resume.ai;

import dev.langchain4j.data.embedding.Embedding;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class DocumentsRepository {

    @Inject
    SessionFactory sessionFactory;

    public DocumentsRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Check if document exists by text
    public boolean documentExistsByText(String text) {
        Session session = sessionFactory.openSession();
        String cypherQuery = "MATCH (d:Document {text: $text}) RETURN count(d) > 0";
        return session.queryForObject(Boolean.class, cypherQuery, Map.of("text", text));
    }

    // Check if document exists by embedding (exact match)
    public boolean documentExistsByEmbedding(Embedding embedding) {
        Session session = sessionFactory.openSession();
        String cypherQuery = "MATCH (d:Document {embedding: $embedding}) RETURN count(d) > 0";
        return session.queryForObject(Boolean.class, cypherQuery, Map.of("embedding", embedding));
    }

    // Retrieve a document by text
    public Document getDocumentByText(String text) {
        Session session = sessionFactory.openSession();
        String cypherQuery = "MATCH (d:Document {text: $text}) RETURN d LIMIT 1";
        return session.queryForObject(Document.class, cypherQuery, Map.of("text", text));
    }

    // Retrieve a document by embedding
    public Document getDocumentByEmbedding(List<Float> embedding) {
        Session session = sessionFactory.openSession();
        String cypherQuery = "MATCH (d:Document {embedding: $embedding}) RETURN d LIMIT 1";
        return session.queryForObject(Document.class, cypherQuery, Map.of("embedding", embedding));
    }
    public Collection<Document> findAll() {
        return sessionFactory.openSession().loadAll(Document.class);
    }
}
