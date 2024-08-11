package com.web.resume.ai;

import org.testcontainers.containers.GenericContainer;

public class PubSubContainer extends GenericContainer<PubSubContainer> {

    public static final String IMAGE = "google/cloud-sdk:alpine";
    public static final int PUBSUB_PORT = 8085;

    public PubSubContainer() {
        super(IMAGE);
        withExposedPorts(PUBSUB_PORT);
        withCommand("gcloud beta emulators pubsub start --host-port=0.0.0.0:8085");
    }

    public String getPubSubUrl() {
        return "http://" + getHost() + ":" + getMappedPort(PUBSUB_PORT);
    }
}
