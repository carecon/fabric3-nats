package org.fabric3.binding.nats.provision;

import java.net.URI;
import java.util.List;

import org.fabric3.spi.model.physical.PhysicalConnectionTarget;

/**
 *
 */
public class NATSConnectionTarget extends PhysicalConnectionTarget {
    private URI channelUri;
    private String defaultTopic;
    private final String serializer;
    private List<String> hosts;

    public NATSConnectionTarget(URI channelUri, String defaultTopic, String serializer, List<String> hosts) {
        this.channelUri = channelUri;
        this.defaultTopic = defaultTopic;
        this.serializer = serializer;
        this.hosts = hosts;
    }

    public URI getChannelUri() {
        return channelUri;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public String getSerializer() {
        return serializer;
    }

}
