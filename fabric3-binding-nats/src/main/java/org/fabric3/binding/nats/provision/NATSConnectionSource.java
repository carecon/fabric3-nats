package org.fabric3.binding.nats.provision;

import java.net.URI;

import org.fabric3.spi.model.physical.PhysicalConnectionSource;

/**
 *
 */
public class NATSConnectionSource extends PhysicalConnectionSource {
    private URI channelUri;
    private String consumerId;
    private String defaultTopic;
    private NATSData data;

    public NATSConnectionSource(URI uri, URI channelUri, String consumerId, String defaultTopic, NATSData data) {
        setUri(uri);
        this.channelUri = channelUri;
        this.consumerId = consumerId;
        this.defaultTopic = defaultTopic;
        this.data = data;
    }

    public URI getChannelUri() {
        return channelUri;
    }

    public String getSourceId() {
        return channelUri + "_Nats_source";
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public NATSData getData() {
        return data;
    }
}
