package org.fabric3.binding.nats.provision;

import java.net.URI;
import java.util.Map;

import org.fabric3.spi.model.physical.PhysicalConnectionTarget;

/**
 *
 */
public class NATSConnectionTarget extends PhysicalConnectionTarget {
    private URI channelUri;
    private String defaultTopic;
    private final String serializer;
    private Map<String, Object> configuration;

    public NATSConnectionTarget(URI channelUri, String defaultTopic, String serializer, Map<String, Object> configuration) {
        this.channelUri = channelUri;
        this.defaultTopic = defaultTopic;
        this.serializer = serializer;
        this.configuration = configuration;
    }

    public URI getChannelUri() {
        return channelUri;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public String getSerializer() {
        return serializer;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

}
