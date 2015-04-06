package org.fabric3.test.binding.nats;

import nats.client.Nats;
import nats.client.NatsConnector;
import nats.client.Subscription;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class TestConsumer {

    @Test
    @Ignore
    public void test() throws Exception {
        NatsConnector connector = new NatsConnector();
        connector.addHost("nats://localhost:4222");
        Nats nats = connector.connect();
        Thread.sleep(2000);
        System.out.println(nats.isConnected());
        Subscription subscribe = nats.subscribe("control.domain.vm", message -> {
            System.out.println(message.getBody());
        });
        Thread.sleep(Long.MAX_VALUE);
    }
}
