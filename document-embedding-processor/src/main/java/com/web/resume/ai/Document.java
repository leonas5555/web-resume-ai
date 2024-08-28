package com.web.resume.ai;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.util.List;

@NodeEntity(label = "com.web.resume.ai.Document")
public class Document {

    @Id
    @GeneratedValue
    private Long id;  // Neo4j auto-generated internal ID

    @Property(name = "embedding")
    private List<Float> embedding;

    @Property(name = "id")
    private String documentId;  // ID from the document node

    @Property(name = "text")
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", embedding=" + embedding +
                ", documentId='" + documentId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
