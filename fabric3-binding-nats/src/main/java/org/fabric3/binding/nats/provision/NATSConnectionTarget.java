package org.fabric3.binding.nats.provision;

import java.net.URI;

import org.fabric3.spi.model.physical.PhysicalConnectionTarget;

/**
 *
 */
public class NATSConnectionTarget extends PhysicalConnectionTarget {
    private URI channelUri;
    private String defaultTopic;
    private final String serializer;
    private NATSData data;

    public NATSConnectionTarget(URI channelUri, String defaultTopic, String serializer, NATSData data) {
        this.channelUri = channelUri;
        this.defaultTopic = defaultTopic;
        this.serializer = serializer;
        this.data = data;
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

    public NATSData getData() {
        return data;
    }

}
