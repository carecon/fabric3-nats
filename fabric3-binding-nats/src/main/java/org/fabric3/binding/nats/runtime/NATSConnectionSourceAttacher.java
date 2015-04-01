package org.fabric3.binding.nats.runtime;

import org.fabric3.api.annotation.wire.Key;
import org.fabric3.binding.nats.provision.NATSConnectionSource;
import org.fabric3.spi.container.builder.component.SourceConnectionAttacher;
import org.fabric3.spi.container.channel.ChannelConnection;
import org.fabric3.spi.model.physical.PhysicalConnectionTarget;
import org.oasisopen.sca.annotation.Reference;

/**
 *
 */
@Key("org.fabric3.binding.nats.provision.NATSConnectionSource")
public class NATSConnectionSourceAttacher implements SourceConnectionAttacher<NATSConnectionSource> {

    @Reference
    protected NATSConnectionManager connectionManager;

    public void attach(NATSConnectionSource source, PhysicalConnectionTarget target, ChannelConnection connection) {
        connection.setCloseable(() -> connectionManager.release(source.getChannelUri()));
        if (target.isDirectConnection()) {
            Class<?> type = target.getServiceInterface();
            connectionManager.createDirectConsumer(type, source); // create consumer, which will be returned by the direct connection factory
        } else {
            connectionManager.subscribe(source, connection);
        }
    }

    public void detach(NATSConnectionSource source, PhysicalConnectionTarget target) {
        connectionManager.release(source.getChannelUri());
    }
}
