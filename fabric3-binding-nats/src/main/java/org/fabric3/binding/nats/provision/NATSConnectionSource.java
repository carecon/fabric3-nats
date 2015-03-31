package org.fabric3.binding.nats.provision;

import java.net.URI;
import java.util.List;

import org.fabric3.spi.model.physical.PhysicalConnectionSource;

/**
 *
 */
public class NATSConnectionSource extends PhysicalConnectionSource {
    private URI channelUri;
    private URI consumerUri;
    private String defaultTopic;
    private final String deserializer;
    private List<String> hosts;

    public NATSConnectionSource(URI channelUri, URI consumerUri, String defaultTopic, String deserializer, List<String> hosts) {
        this.channelUri = channelUri;
        this.consumerUri = consumerUri;
        this.defaultTopic = defaultTopic;
        this.deserializer = deserializer;
        this.hosts = hosts;
    }

    public URI getChannelUri() {
        return channelUri;
    }

    public URI getConsumerUri() {
        return consumerUri;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public String getDeserializer() {
        return deserializer;
    }

}
