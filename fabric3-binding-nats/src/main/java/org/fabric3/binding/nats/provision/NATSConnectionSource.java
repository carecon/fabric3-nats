package org.fabric3.binding.nats.provision;

import java.net.URI;

import org.fabric3.spi.model.physical.PhysicalConnectionSource;

/**
 *
 */
public class NATSConnectionSource extends PhysicalConnectionSource {
    private URI channelUri;
    private URI consumerUri;
    private String defaultTopic;
    private final String deserializer;
    private NATSData data;

    public NATSConnectionSource(URI channelUri, URI consumerUri, String defaultTopic, String deserializer, NATSData data) {
        this.channelUri = channelUri;
        this.consumerUri = consumerUri;
        this.defaultTopic = defaultTopic;
        this.deserializer = deserializer;
        this.data = data;
    }

    public URI getChannelUri() {
        return channelUri;
    }

    public URI getConsumerUri() {
        return consumerUri;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public String getDeserializer() {
        return deserializer;
    }

    public NATSData getData() {
        return data;
    }
}
