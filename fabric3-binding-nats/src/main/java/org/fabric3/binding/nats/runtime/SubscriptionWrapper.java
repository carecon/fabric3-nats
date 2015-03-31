package org.fabric3.binding.nats.runtime;

import java.net.URI;
import java.util.Spliterator;
import java.util.function.Consumer;

import nats.client.Message;
import nats.client.MessageHandler;
import nats.client.MessageIterator;
import nats.client.Registration;
import nats.client.Subscription;

/**
 *
 */
public class SubscriptionWrapper implements Subscription {
    private Subscription delegate;
    private URI channelUri;
    private NATSConnectionManager connectionManager;

    public SubscriptionWrapper(Subscription delegate, URI channelUri, NATSConnectionManager connectionManager) {
        this.delegate = delegate;
        this.channelUri = channelUri;
        this.connectionManager = connectionManager;
    }

    public void close() {
        delegate.close();
        connectionManager.release(channelUri);
    }

    public String getSubject() {
        return delegate.getSubject();
    }

    public int getReceivedMessages() {
        return delegate.getReceivedMessages();
    }

    public Integer getMaxMessages() {
        return delegate.getMaxMessages();
    }

    public String getQueueGroup() {
        return delegate.getQueueGroup();
    }

    public MessageIterator iterator() {
        return delegate.iterator();
    }

    public void forEach(Consumer<? super Message> action) {
        delegate.forEach(action);
    }

    public Spliterator<Message> spliterator() {
        return delegate.spliterator();
    }

    public Registration addMessageHandler(MessageHandler messageHandler) {
        return delegate.addMessageHandler(messageHandler);
    }
}
