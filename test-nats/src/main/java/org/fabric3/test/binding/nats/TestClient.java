package org.fabric3.test.binding.nats;

import java.util.concurrent.CountDownLatch;

import org.fabric3.api.implementation.junit.Fabric3Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(Fabric3Runner.class)
public class TestClient {
    private static final CountDownLatch LATCH = new CountDownLatch(1);

//    @Producer(target = "NATSChannel")
//    protected Nats producer;
//
//    @Producer(target = "NATSChannel")
//    protected TestChannel channel;
//
//    @Consumer(source = "NATSChannel")
//    protected Subscription subscription;
//
//    @Channel("NATSChannel")
//    protected ChannelContext channelContext;
//
//    @Consumer(source = "NATSChannel")
//    public void receive(String message) {
//        System.out.println("Received!!!!!!!:" + message);
//        LATCH.countDown();
//    }

    @Test
    public void testProduce() throws Exception {
//        subscription.addMessageHandler((m) -> System.out.println("injected::::::::::" + m.getBody()));
//        Subscription dynamicSubscription = channelContext.getConsumer(Subscription.class, "test");
//        dynamicSubscription.addMessageHandler((m) -> System.out.println("::::::::::" + m.getBody()));
//        channel.publish("hello");
//        producer.publish("test", "hello-producer");
//        LATCH.await();
//        System.out.println("Done");
    }
}
