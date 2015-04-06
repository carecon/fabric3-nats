package org.fabric3.binding.nats.provision;

import java.net.URI;

import org.fabric3.spi.model.physical.PhysicalConnectionTarget;

/**
 *
 */
public class NATSConnectionTarget extends PhysicalConnectionTarget {
    private URI channelUri;
    private String defaultTopic;
    private NATSData data;

    public NATSConnectionTarget(URI uri, URI channelUri, String defaultTopic, NATSData data) {
        setUri(uri);
        this.channelUri = channelUri;
        this.defaultTopic = defaultTopic;
        this.data = data;
    }

    public URI getChannelUri() {
        return channelUri;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public NATSData getData() {
        return data;
    }

}
