package org.fabric3.binding.nats.monitor;

import java.util.List;

import org.fabric3.monitor.spi.model.type.AppenderDefinition;

/**
 *
 */
public class NATSAppenderDefinition extends AppenderDefinition {
    private String topic;
    private List<String> hosts;

    public NATSAppenderDefinition(String topic, List<String> hosts) {
        super("nats");
        this.topic = topic;
        this.hosts = hosts;
    }

    public String getTopic() {
        return topic;
    }

    public List<String> getHosts() {
        return hosts;
    }
}
