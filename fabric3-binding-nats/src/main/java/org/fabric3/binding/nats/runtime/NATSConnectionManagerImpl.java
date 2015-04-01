package org.fabric3.binding.nats.runtime;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Supplier;

import nats.client.Nats;
import nats.client.NatsConnector;
import nats.client.Subscription;
import org.fabric3.api.host.Fabric3Exception;
import org.fabric3.api.host.runtime.HostInfo;
import org.fabric3.binding.nats.provision.NATSConnectionSource;
import org.fabric3.binding.nats.provision.NATSConnectionTarget;
import org.fabric3.spi.container.builder.component.DirectConnectionFactory;
import org.fabric3.spi.container.channel.ChannelConnection;
import org.fabric3.spi.container.component.ComponentManager;
import org.fabric3.spi.util.Cast;
import org.oasisopen.sca.annotation.Destroy;
import org.oasisopen.sca.annotation.EagerInit;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Service;

/**
 *
 */
@EagerInit
@Service({NATSConnectionManager.class, DirectConnectionFactory.class})
public class NATSConnectionManagerImpl implements NATSConnectionManager, DirectConnectionFactory {
    private static final List<Class<?>> TYPES = Arrays.asList(Nats.class, Subscription.class);
    private static final String DEFAULT_HOST = "nats://localhost:4222";

    private Map<URI, Holder> connections = new HashMap<>();

    @Reference
    protected HostInfo info;

    @Reference
    protected ComponentManager cm;

    @Reference
    protected ExecutorService executorService;

    @Init
    public void init() {
    }

    @Destroy
    public void destroy() {
        for (Holder holder : connections.values()) {
            holder.delegate.closeUnderyling();
        }
    }

    public List<Class<?>> getTypes() {
        return TYPES;
    }

    public Nats getNats(NATSConnectionTarget target) {
        return connections.computeIfAbsent(target.getChannelUri(), (s) -> createNats(target)).delegate;
    }

    public void release(URI channelUri, String topic) {
        release(channelUri, topic, true);
    }

    public <T> T createDirectConsumer(Class<T> type, NATSConnectionSource source) {
        URI channelUri = source.getChannelUri();
        if (Nats.class.isAssignableFrom(type)) {
            return Cast.cast(connections.computeIfAbsent(channelUri, (s) -> createNats(source)).delegate);
        } else if (Subscription.class.isAssignableFrom(type)) {
            Nats nats = connections.computeIfAbsent(channelUri, (s) -> createNats(source)).delegate;
            String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
            Subscription subscription = nats.subscribe(topic);
            return Cast.cast(new SubscriptionWrapper(subscription, channelUri, topic, this));
        } else {
            throw new Fabric3Exception("Invalid consumer type: " + type.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public void subscribe(NATSConnectionSource source, ChannelConnection connection) {
        URI channelUri = source.getChannelUri();
        String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();

        Holder holder = connections.get(channelUri);

        boolean exists = holder != null;

        // only create the subscription
        Nats nats;
        if (!exists) {
            holder = createNats(source);
        } else {
            holder.counter++;
        }
        nats = holder.delegate;

        if (!exists || !holder.topics.contains(topic)) {
            // Only create a subscription if one does not exist; otherwise targets will receive duplicate messages as multiple subscriptions will feed
            // into the channel event stream
            String deserializerName = source.getDeserializer();
            Function deserializer = deserializerName != null ? InstanceResolver.getInstance(deserializerName, info, cm) : null;

            connections.put(channelUri, holder);
            Subscription subscription = nats.subscribe(topic, message -> {
                Object body = deserializer != null ? deserializer.apply(message.getBody()) : message.getBody();
                connection.getEventStream().getHeadHandler().handle(body, false);
            });

            // set the closeable callback
            connection.setCloseable(() -> {
                subscription.close();
                release(channelUri, topic);
            });
            holder.topics.add(topic);
        } else {
            connection.setCloseable(() -> {
                release(channelUri, topic);
            });
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Supplier<T> getConnection(URI channelUri, URI attachUri, Class<T> type) {
        // Nats is created by createDirectConsumer(..)
        if (Nats.class.isAssignableFrom(type)) {
            return () -> Cast.cast(connections.get(channelUri).delegate);
        } else if (Subscription.class.isAssignableFrom(type)) {
            Holder holder = connections.get(channelUri);
            return () -> {
                Subscription subscription = holder.delegate.subscribe(holder.topic);
                return Cast.cast(new SubscriptionWrapper(subscription, channelUri, holder.topic, this));
            };
        } else {
            throw new Fabric3Exception("Invalid connection type: " + type.getName());
        }
    }

    public <T> Supplier<T> getConnection(URI uri, URI attachUri, Class<T> type, String topic) {
        return getConnection(uri, attachUri, type);
    }

    @SuppressWarnings("unchecked")
    private Holder createNats(NATSConnectionTarget target) {
        NatsConnector connector = new NatsConnector();
        List<String> hosts = target.getHosts();
        if (hosts.isEmpty()) {
            connector.addHost(DEFAULT_HOST);
        } else {
            hosts.forEach(connector::addHost);
        }
        connector.calllbackExecutor(executorService);
        Nats nats = connector.connect();
        URI channelUri = target.getChannelUri();
        String topic = target.getTopic() != null ? target.getTopic() : target.getDefaultTopic();
        NatsWrapper wrapper = new NatsWrapper(nats, channelUri, this);
        return new Holder(wrapper, topic);
    }

    private Holder createNats(NATSConnectionSource source) {
        NatsConnector connector = new NatsConnector();
        List<String> hosts = source.getHosts();
        if (hosts.isEmpty()) {
            connector.addHost(DEFAULT_HOST);
        } else {
            hosts.forEach(connector::addHost);
        }
        connector.calllbackExecutor(executorService);
        Nats nats = connector.connect();
        URI channelUri = source.getChannelUri();
        String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
        NatsWrapper wrapper = new NatsWrapper(nats, channelUri, this);
        return new Holder(wrapper, topic);
    }

    private Nats release(URI channelUri, String topic, boolean shutdown) {
        Holder holder = connections.get(channelUri);
        if (holder == null) {
            return null;
        }
        if (--holder.counter == 0) {
            if (shutdown) {
                holder.delegate.close();
            }
            if (topic != null) {
                holder.topics.remove(topic);
            }
            connections.remove(channelUri);
        }
        return holder.delegate;
    }

    private class Holder {
        NatsWrapper delegate;
        int counter = 1;
        String topic;
        Set<String> topics = new HashSet<>();

        public Holder(NatsWrapper delegate, String topic) {
            this.delegate = delegate;
            this.topic = topic;
        }

    }
}
