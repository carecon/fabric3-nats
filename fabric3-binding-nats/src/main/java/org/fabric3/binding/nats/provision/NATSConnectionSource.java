package org.fabric3.binding.nats.provision;

import java.net.URI;
import java.util.Map;

import org.fabric3.spi.model.physical.PhysicalConnectionSource;

/**
 *
 */
public class NATSConnectionSource extends PhysicalConnectionSource {
    private URI channelUri;
    private URI consumerUri;
    private String defaultTopic;
    private final String deserializer;

    private Map<String, Object> configuration;

    public NATSConnectionSource(URI channelUri, URI consumerUri, String defaultTopic, String deserializer, Map<String, Object> configuration) {
        this.channelUri = channelUri;
        this.consumerUri = consumerUri;
        this.defaultTopic = defaultTopic;
        this.deserializer = deserializer;
        this.configuration = configuration;
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

    public Map<String, Object> getConfiguration() {
        return configuration;
    }
}
