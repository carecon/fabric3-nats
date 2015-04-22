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
        for (int i = 0; i < 1; i++) {
            nats.publish("domain.notification",
                         "{\"type\":\"notification\", \"priority\":\"NORMAL\", \"detail\":\"Shutdown in 10 minutes\", " + "\"notificationId\":\"1\"}");
            Thread.sleep(10);
        }
        nats.publish("domain.notification",
                     "{\"type\":\"notification\", \"priority\":\"HIGH\", \"detail\":\"Repository updated\", " + "\"notificationId\":\"1\"}");

        //        for (int i = 0; i < 1; i++) {
        //            System.out.println(i);
        //            nats.publish("control.domain.vm", "{\"type\":\"control::runtime::heartbeat\", \"id\":\"123\"}");
        //            Thread.sleep(10);
        //        }
        // nats.publish("control.domain.vm", "{\"type\":\"control::runtime::shutdown\"}");
    }
}
