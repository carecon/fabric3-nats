package org.fabric3.binding.nats.runtime;

import nats.client.Nats;
import org.fabric3.api.annotation.wire.Key;
import org.fabric3.binding.nats.provision.NATSConnectionTarget;
import org.fabric3.spi.container.builder.component.TargetConnectionAttacher;
import org.fabric3.spi.container.channel.ChannelConnection;
import org.fabric3.spi.model.physical.PhysicalConnectionSource;
import org.oasisopen.sca.annotation.Reference;

/**
 *
 */
@Key("org.fabric3.binding.nats.provision.NATSConnectionTarget")
public class NATSConnectionTargetAttacher implements TargetConnectionAttacher<NATSConnectionTarget> {

    @Reference
    protected NATSConnectionManager connectionManager;

    @SuppressWarnings("unchecked")
    public void attach(PhysicalConnectionSource source, NATSConnectionTarget target, ChannelConnection connection) {
        Nats nats = connectionManager.getNats(target);
        if (!source.isDirectConnection()) {
            String topic = target.getTopic() != null ? target.getTopic() : target.getDefaultTopic();
            connection.getEventStream().addHandler((event, batch) -> nats.publish(topic, event.toString()));
            connection.getEventStream().setCloseable(() -> connectionManager.release(target.getChannelUri()));
        }
    }

    public void detach(PhysicalConnectionSource source, NATSConnectionTarget target) {
        connectionManager.release(target.getChannelUri());
    }
}
