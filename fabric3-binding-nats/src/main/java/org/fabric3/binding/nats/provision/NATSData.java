package org.fabric3.binding.nats.provision;

import java.util.List;

/**
 *
 */
public class NATSData {
    private boolean automaticReconnect;
    private long reconnectWaitTime;
    private boolean pedantic;
    private int maxFrameSize;

    private List<String> hosts;

    public boolean isAutomaticReconnect() {
        return automaticReconnect;
    }

    public long getReconnectWaitTime() {
        return reconnectWaitTime;
    }

    public boolean isPedantic() {
        return pedantic;
    }

    public int getMaxFrameSize() {
        return maxFrameSize;
    }

    public List<String> getHosts() {
        return hosts;
    }

    void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    void setAutomaticReconnect(boolean automaticReconnect) {
        this.automaticReconnect = automaticReconnect;
    }

    void setReconnectWaitTime(long reconnectWaitTime) {
        this.reconnectWaitTime = reconnectWaitTime;
    }

    void setPedantic(boolean pedantic) {
        this.pedantic = pedantic;
    }

    void setMaxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    void addHost(String host) {
        hosts.add(host);
    }

    public static class Builder {
        private NATSData data;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder automaticReconnect(boolean value) {
            data.setAutomaticReconnect(value);
            return this;
        }

        public Builder reconnectWaitTime(long time) {
            data.setReconnectWaitTime(time);
            return this;
        }

        public Builder pedantic(boolean pedantic) {
            data.setPedantic(pedantic);
            return this;
        }

        public Builder maxFrameSize(int size) {
            data.setMaxFrameSize(size);
            return this;
        }

        public NATSData build() {
            return data;
        }

        public Builder hosts(List<String> hosts) {
            data.setHosts(hosts);
            return this;
        }

        private Builder() {
           data = new NATSData();
        }

    }
}
