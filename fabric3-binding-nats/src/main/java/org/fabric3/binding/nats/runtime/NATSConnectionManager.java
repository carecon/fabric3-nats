package org.fabric3.binding.nats.runtime;

import java.net.URI;

import nats.client.Nats;
import org.fabric3.binding.nats.provision.NATSConnectionSource;
import org.fabric3.binding.nats.provision.NATSConnectionTarget;
import org.fabric3.spi.container.channel.ChannelConnection;

/**
 * Manages producer and consumer connections to a NATS cluster.
 */
public interface NATSConnectionManager {

    /**
     * Returns a Nats connection for the target configuration.
     *
     * @param target the target configuration
     * @return the Nats connection
     */
    Nats getNats(NATSConnectionTarget target);

    /**
     * Releases resources used by a channel.
     *
     * @param channelUri the channel uri
     * @param topic      the topic or null if not available
     */
    void release(URI channelUri, String topic);

    /**
     * Creates a direct consumer connection.
     *
     * @param type   the connection type
     * @param source the source configuration
     * @return the connection
     */
    <T> T createDirectConsumer(Class<T> type, NATSConnectionSource source);

    /**
     * Subscribes the channel connection to a NATS topic.
     *
     * @param source the source configuration
     */
    void subscribe(NATSConnectionSource source, ChannelConnection connection);

}
