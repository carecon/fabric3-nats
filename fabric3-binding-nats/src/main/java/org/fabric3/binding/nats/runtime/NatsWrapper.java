package org.fabric3.binding.nats.runtime;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import nats.client.MessageHandler;
import nats.client.Nats;
import nats.client.Registration;
import nats.client.Request;
import nats.client.Subscription;

/**
 *
 */
public class NatsWrapper implements Nats {
    private Nats delegate;
    private URI channelUri;
    private NATSConnectionManager connectionManager;

    public NatsWrapper(Nats delegate, URI channelUri, NATSConnectionManager connectionManager) {
        this.delegate = delegate;
        this.channelUri = channelUri;
        this.connectionManager = connectionManager;
    }

    public boolean isConnected() {
        return delegate.isConnected();
    }

    public boolean isClosed() {
        return delegate.isClosed();
    }

    public void close() {
        connectionManager.release(channelUri);
    }

    public void publish(String subject) {
        delegate.publish(subject);
    }

    public void publish(String subject, String body) {
        delegate.publish(subject, body);
    }

    public void publish(String subject, String body, String replyTo) {
        delegate.publish(subject, body, replyTo);
    }

    public Registration publish(String subject, long period, TimeUnit unit) {
        return delegate.publish(subject, period, unit);
    }

    public Registration publish(String subject, String body, long period, TimeUnit unit) {
        return delegate.publish(subject, body, period, unit);
    }

    public Registration publish(String subject, String body, String replyTo, long period, TimeUnit unit) {
        return delegate.publish(subject, body, replyTo, period, unit);
    }

    public Subscription subscribe(String subject, MessageHandler... messageHandlers) {
        return delegate.subscribe(subject, messageHandlers);
    }

    public Subscription subscribe(String subject, String queueGroup, MessageHandler... messageHandlers) {
        return delegate.subscribe(subject, queueGroup, messageHandlers);
    }

    public Subscription subscribe(String subject, Integer maxMessages, MessageHandler... messageHandlers) {
        return delegate.subscribe(subject, maxMessages, messageHandlers);
    }

    public Subscription subscribe(String subject, String queueGroup, Integer maxMessages, MessageHandler... messageHandlers) {
        return delegate.subscribe(subject, queueGroup, maxMessages, messageHandlers);
    }

    public Request request(String subject, long timeout, TimeUnit unit, MessageHandler... messageHandlers) {
        return delegate.request(subject, timeout, unit, messageHandlers);
    }

    public Request request(String subject, String message, long timeout, TimeUnit unit, MessageHandler... messageHandlers) {
        return delegate.request(subject, message, timeout, unit, messageHandlers);
    }

    public Request request(String subject, String message, long timeout, TimeUnit unit, Integer maxReplies, MessageHandler... messageHandlers) {
        return delegate.request(subject, message, timeout, unit, maxReplies, messageHandlers);
    }

    public void closeUnderyling() {
        delegate.close();
    }
}
