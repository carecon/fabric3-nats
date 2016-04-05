package org.fabric3.test.binding.nats;

import java.util.concurrent.CountDownLatch;

import nats.client.Nats;
import nats.client.Subscription;
import org.fabric3.api.ChannelContext;
import org.fabric3.api.annotation.Channel;
import org.fabric3.api.annotation.Consumer;
import org.fabric3.api.annotation.Producer;
import org.fabric3.api.implementation.junit.Fabric3Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(Fabric3Runner.class)
public class TestClient {
    private static final CountDownLatch RECEIVE_LATCH = new CountDownLatch(1);
    private static final CountDownLatch SUBSCRIBE_LATCH = new CountDownLatch(1);
    private static final CountDownLatch SUBSCRIPTION_LATCH = new CountDownLatch(1);
    private static final CountDownLatch DYNAMIC_LATCH = new CountDownLatch(1);

    @Producer(target = "NATSChannel")
    protected Nats producer;

    @Producer(target = "NATSChannel")
    protected TestChannel channel;

    @Consumer(source = "NATSChannel")
    protected Subscription subscription;

    @Channel("NATSChannel")
    protected ChannelContext channelContext;

    @Consumer(source = "NATSChannel")
    public void receive(String message) {
        System.out.println("Received on message default subscribe:" + message);
        RECEIVE_LATCH.countDown();
    }

    @Test
    public void testProduce() throws Exception {
        if (true) {
            Thread.sleep(1000);
            System.out.println("********** Tests disabled. Skipping tests.");
            return;
        }
        Object handle = channelContext.subscribe(String.class, "id", "test", m -> {
            System.out.println("Received in 'test' subscribe:" + m);
            SUBSCRIBE_LATCH.countDown();
        });

        subscription.addMessageHandler((m) -> {
            System.out.println("Received in injected default subscription:" + m);
            SUBSCRIPTION_LATCH.countDown();
        });

        Subscription dynamicSubscription = channelContext.getConsumer(Subscription.class, "test");
        dynamicSubscription.addMessageHandler((m) -> {
            System.out.println("Received in dynamic 'test' subscription:" + m);
            DYNAMIC_LATCH.countDown();
        });

        Subscription dynamicSubscription2 = channelContext.getConsumer(Subscription.class, "test2");
        dynamicSubscription2.addMessageHandler((m) -> {
            System.out.println("Should NOT receive dynamic subscription:" + m);
            throw new AssertionError("Test topic failed");
        });

        channel.publish("\"hello\"");
        producer.publish("test", "\"hello2\"");

        RECEIVE_LATCH.await();
        SUBSCRIBE_LATCH.await();
        SUBSCRIPTION_LATCH.await();
        DYNAMIC_LATCH.await();

        channelContext.close(handle);
        subscription.close();

        System.out.println("Done");
        Thread.sleep(5000);   // avoid async shutdown errors
    }
}
