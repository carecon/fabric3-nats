package org.fabric3.binding.nats.runtime;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.fabric3.spi.container.component.Component;
import org.fabric3.spi.container.component.ComponentManager;
import org.fabric3.spi.container.component.ScopedComponent;
import org.fabric3.spi.runtime.event.EventService;
import org.fabric3.spi.runtime.event.RuntimeStart;
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
    public static final String DEFAULT_HOST = "nats://localhost:4222";

    private Map<URI, Holder> connections = new HashMap<>();

    @Reference
    protected HostInfo info;

    @Reference
    protected ComponentManager cm;

    @Reference
    protected EventService eventService;

    @Reference
    protected ExecutorService executorService;

    private List<Runnable> queuedSubscriptions = new ArrayList<>();

    @Init
    public void init() {
        eventService.subscribe(RuntimeStart.class, (event) -> {
            queuedSubscriptions.forEach(executorService::submit);
            queuedSubscriptions.clear();
        });
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

    public void release(URI channelUri) {
        release(channelUri, true);
    }

    public <T> T createDirectConsumer(Class<T> type, NATSConnectionSource source) {
        URI channelUri = source.getChannelUri();
        if (Nats.class.isAssignableFrom(type)) {
            return Cast.cast(connections.computeIfAbsent(channelUri, (s) -> createNats(source)).delegate);
        } else if (Subscription.class.isAssignableFrom(type)) {
            Nats nats = connections.computeIfAbsent(channelUri, (s) -> createNats(source)).delegate;
            String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
            return Cast.cast(new SubscriptionWrapper(nats.subscribe(topic), channelUri, this));
        } else {
            throw new Fabric3Exception("Invalid consumer type: " + type.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public void subscribe(NATSConnectionSource source, ChannelConnection connection) {
        URI channelUri = source.getChannelUri();
        String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
        Nats nats = connections.computeIfAbsent(channelUri, (s) -> createNats(source)).delegate;
        nats.subscribe(topic, message -> {
            connection.getEventStream().getHeadHandler().handle(message.getBody(), false);
        });
        // set the closeable callback
        connection.getEventStream().setCloseable(() -> release(channelUri));
    }

    @SuppressWarnings("unchecked")
    public <T> Supplier<T> getConnection(URI channelUri, URI attachUri, Class<T> type) {
        // Nats is created by createDirectConsumer(..)
        if (Nats.class.isAssignableFrom(type)) {
            return () -> Cast.cast(connections.get(channelUri).delegate);
        } else if (Subscription.class.isAssignableFrom(type)) {
            Holder holder = connections.get(channelUri);
            return () -> Cast.cast(new SubscriptionWrapper(holder.delegate.subscribe(holder.topic), channelUri, this));
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
        Nats nats = connector.connect();
        URI channelUri = source.getChannelUri();
        String topic = source.getTopic() != null ? source.getTopic() : source.getDefaultTopic();
        NatsWrapper wrapper = new NatsWrapper(nats, channelUri, this);
        return new Holder(wrapper, topic);
    }

    private Nats release(URI channelUri, boolean shutdown) {
        Holder holder = connections.get(channelUri);
        if (holder == null) {
            return null;
        }
        if (--holder.counter == 0) {
            if (shutdown) {
                holder.delegate.close();
            }
            connections.remove(channelUri);
        }
        return holder.delegate;
    }

    private Function getInstance(String name) {
        URI serializerUri = URI.create(info.getDomain().toString() + "/" + name);
        Component component = cm.getComponent(serializerUri);
        if (component == null) {
            throw new Fabric3Exception("Component not found: " + name);
        }
        if (!(component instanceof ScopedComponent)) {
            throw new Fabric3Exception("Component must be a Java component: " + name);
        }
        ScopedComponent scopedComponent = (ScopedComponent) component;
        Object instance = scopedComponent.getInstance();
        if (!(instance instanceof Function)) {
            throw new Fabric3Exception("Serializer must implement: " + Function.class.getName());
        }
        return (Function) instance;
    }

    private class Holder {
        NatsWrapper delegate;
        int counter = 1;
        String topic;

        public Holder(NatsWrapper delegate, String topic) {
            this.delegate = delegate;
            this.topic = topic;
        }

    }
}
