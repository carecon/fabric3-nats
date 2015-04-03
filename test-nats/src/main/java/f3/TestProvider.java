package f3;

import javax.xml.namespace.QName;

import org.fabric3.api.annotation.model.Provides;
import org.fabric3.api.binding.nats.model.NATSBinding;
import org.fabric3.api.binding.nats.model.NATSBindingBuilder;
import org.fabric3.api.model.type.builder.ChannelBuilder;
import org.fabric3.api.model.type.builder.CompositeBuilder;
import org.fabric3.api.model.type.component.Composite;
import org.fabric3.test.binding.nats.JsonDeserializer;
import org.fabric3.test.binding.nats.JsonSerializer;

/**
 *
 */
public class TestProvider {
    private static final QName QNAME = new QName("urn:org.fabric3", "TestComposite");

    @Provides
    public static Composite getComposite() {
        CompositeBuilder compositeBuilder = CompositeBuilder.newBuilder(QNAME);
        ChannelBuilder channelBuilder = ChannelBuilder.newBuilder("NATSChannel");
        NATSBindingBuilder bindingBuilder = NATSBindingBuilder.newBuilder();
        bindingBuilder.defaultTopic("test");
        bindingBuilder.serializer(JsonSerializer.class.getSimpleName());
        bindingBuilder.deserializer(JsonDeserializer.class.getSimpleName());
        NATSBinding binding = bindingBuilder.build();
        channelBuilder.binding(binding);
        compositeBuilder.channel(channelBuilder.build());
        compositeBuilder.deployable();
        return compositeBuilder.build();
    }
}
