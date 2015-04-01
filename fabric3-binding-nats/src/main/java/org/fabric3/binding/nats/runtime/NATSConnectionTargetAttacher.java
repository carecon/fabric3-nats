package org.fabric3.binding.nats.runtime;

import java.util.function.Function;

import nats.client.Nats;
import org.fabric3.api.annotation.wire.Key;
import org.fabric3.api.host.runtime.HostInfo;
import org.fabric3.binding.nats.provision.NATSConnectionTarget;
import org.fabric3.spi.container.builder.component.TargetConnectionAttacher;
import org.fabric3.spi.container.channel.ChannelConnection;
import org.fabric3.spi.container.component.ComponentManager;
import org.fabric3.spi.model.physical.PhysicalConnectionSource;
import org.oasisopen.sca.annotation.Reference;

/**
 *
 */
@Key("org.fabric3.binding.nats.provision.NATSConnectionTarget")
public class NATSConnectionTargetAttacher implements TargetConnectionAttacher<NATSConnectionTarget> {
    @Reference
    protected HostInfo info;

    @Reference
    protected ComponentManager cm;

    @Reference
    protected NATSConnectionManager connectionManager;

    @SuppressWarnings("unchecked")
    public void attach(PhysicalConnectionSource source, NATSConnectionTarget target, ChannelConnection connection) {
        Nats nats = connectionManager.getNats(target);
        if (!source.isDirectConnection()) {
            String topic = target.getTopic() != null ? target.getTopic() : target.getDefaultTopic();
            String serializerName = target.getSerializer();
            Function<Object, String> serializer = serializerName != null ? InstanceResolver.getInstance(serializerName, info, cm) : null;

            connection.getEventStream().addHandler((event, batch) -> {
                String serialized = serializer != null ? serializer.apply(event) : event.toString();
                nats.publish(topic, serialized);
            });
            connection.setCloseable(() -> connectionManager.release(target.getChannelUri(), topic));
        }
    }

    public void detach(PhysicalConnectionSource source, NATSConnectionTarget target) {
        String topic = target.getTopic() != null ? target.getTopic() : target.getDefaultTopic();
        connectionManager.release(target.getChannelUri(), topic);
    }
}
