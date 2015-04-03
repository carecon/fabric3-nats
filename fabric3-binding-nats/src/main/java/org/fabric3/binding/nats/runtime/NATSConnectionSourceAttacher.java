package org.fabric3.binding.nats.runtime;

import java.util.function.Function;

import org.fabric3.api.annotation.wire.Key;
import org.fabric3.api.host.runtime.HostInfo;
import org.fabric3.binding.nats.provision.NATSConnectionSource;
import org.fabric3.spi.container.builder.component.SourceConnectionAttacher;
import org.fabric3.spi.container.channel.ChannelConnection;
import org.fabric3.spi.container.channel.EventStreamHandler;
import org.fabric3.spi.container.component.ComponentManager;
import org.fabric3.spi.model.physical.PhysicalConnectionTarget;
import org.oasisopen.sca.annotation.Reference;

/**
 *
 */
@Key("org.fabric3.binding.nats.provision.NATSConnectionSource")
public class NATSConnectionSourceAttacher implements SourceConnectionAttacher<NATSConnectionSource> {
    @Reference
    protected HostInfo info;

    @Reference
    protected ComponentManager cm;

    @Reference
    protected NATSConnectionManager connectionManager;

    public void attach(NATSConnectionSource source, PhysicalConnectionTarget target, ChannelConnection connection) {
        String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
        connection.setCloseable(() -> connectionManager.release(source.getChannelUri(), topic));
        if (target.isDirectConnection()) {
            Class<?> type = target.getServiceInterface();
            connectionManager.createDirectConsumer(type, source); // create consumer, which will be returned by the direct connection factory
        } else {
            String deserializerName = source.getData().getDeserializer();
            Function deserializer = deserializerName != null ? InstanceResolver.getInstance(deserializerName, info, cm) : null;
            connection.getEventStream().addHandler(0, new EventStreamHandler() {
                private EventStreamHandler next;
                public void handle(Object message, boolean batch) {
                    Object body = deserializer != null ? deserializer.apply(message) : message;
                    next.handle(body, batch);
                }

                public void setNext(EventStreamHandler next) {
                    this.next = next;
                }

                public EventStreamHandler getNext() {
                    return next;
                }
            });
            connectionManager.subscribe(source, connection);
        }
    }

    public void detach(NATSConnectionSource source, PhysicalConnectionTarget target) {
        String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
        connectionManager.release(source.getChannelUri(), topic);
    }
}
