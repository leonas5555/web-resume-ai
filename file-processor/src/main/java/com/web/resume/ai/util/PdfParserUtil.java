package com.web.resume.ai.util;

import io.quarkus.tika.TikaParser;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;

@ApplicationScoped
public class PdfParserUtil {

    @Inject
    TikaParser parser;
    @Inject
    Vertx vertx;

    /**
     * Asynchronous method to extract text from an InputStream (which could be a PDF file).
     * @param inputStream InputStream of the file (PDF) to extract text from.
     * @return Uni<String> representing a reactive type that will eventually provide the extracted text asynchronously.
     */
    public Uni<String> extractTextAsync(InputStream inputStream) {
        return vertx.executeBlocking(() -> parser.getText(inputStream));
    }
}
