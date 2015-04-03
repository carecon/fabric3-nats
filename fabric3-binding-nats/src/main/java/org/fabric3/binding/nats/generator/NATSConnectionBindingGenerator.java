package org.fabric3.binding.nats.generator;

import java.net.URI;

import org.fabric3.api.annotation.wire.Key;
import org.fabric3.api.binding.nats.model.NATSBinding;
import org.fabric3.binding.nats.provision.NATSConnectionSource;
import org.fabric3.binding.nats.provision.NATSConnectionTarget;
import org.fabric3.binding.nats.provision.NATSData;
import org.fabric3.spi.domain.generator.channel.ConnectionBindingGenerator;
import org.fabric3.spi.model.instance.LogicalBinding;
import org.fabric3.spi.model.instance.LogicalConsumer;
import org.fabric3.spi.model.instance.LogicalProducer;
import org.fabric3.spi.model.physical.DeliveryType;
import org.fabric3.spi.model.physical.PhysicalConnectionSource;
import org.fabric3.spi.model.physical.PhysicalConnectionTarget;
import org.oasisopen.sca.annotation.EagerInit;

/**
 *
 */
@EagerInit
@Key("org.fabric3.api.binding.nats.model.NATSBinding")
public class NATSConnectionBindingGenerator implements ConnectionBindingGenerator<NATSBinding> {

    public PhysicalConnectionSource generateConnectionSource(LogicalConsumer consumer, LogicalBinding<NATSBinding> binding, DeliveryType deliveryType) {
        NATSBinding natsBinding = binding.getDefinition();
        URI channelUri = binding.getParent().getUri();
        String defaultTopic = natsBinding.getDefaultTopic();

        NATSData natsData = generateData(natsBinding);

        return new NATSConnectionSource(channelUri, defaultTopic, natsData);
    }

    public PhysicalConnectionTarget generateConnectionTarget(LogicalProducer producer, LogicalBinding<NATSBinding> binding, DeliveryType deliveryType) {
        NATSBinding natsBinding = binding.getDefinition();
        URI channelUri = binding.getParent().getUri();
        String defaultTopic = natsBinding.getDefaultTopic();

        NATSData natsData = generateData(natsBinding);

        return new NATSConnectionTarget(channelUri, defaultTopic, natsData);
    }

    private NATSData generateData(NATSBinding binding) {
        NATSData.Builder builder = NATSData.Builder.newBuilder();
        builder.hosts(binding.getHosts());
        builder.automaticReconnect(binding.isAutomaticReconnect());
        builder.maxFrameSize(binding.getMaxFrameSize());
        builder.pedantic(binding.isPedantic());
        builder.reconnectWaitTime(binding.getReconnectWaitTime());
        String serializer = binding.getSerializer();
        builder.serializer(serializer);
        String deserializer = binding.getDeserializer();
        builder.deserializer(deserializer);
        return builder.build();
    }
}
