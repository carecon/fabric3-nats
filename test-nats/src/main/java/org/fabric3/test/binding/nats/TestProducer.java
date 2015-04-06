package org.fabric3.test.binding.nats;

import nats.client.Nats;
import nats.client.NatsConnector;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class TestProducer {

    @Test
    @Ignore
    public void test() throws Exception {
        NatsConnector connector = new NatsConnector();
        connector.addHost("nats://localhost:4222");
        Nats nats = connector.connect();
        Thread.sleep(2000);
        System.out.println(nats.isConnected());
        nats.publish("control.domain.vm", "{\"type\":\"control::runtime::heartbeat\", \"id\":\"123\", \"replyTo\":\"domain.vm\"}");
//        nats.publish("control.domain.vm", "{\"type\":\"control::runtime::shutdown\"}");
    }
}
