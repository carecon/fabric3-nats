/*
 * Fabric3
 * Copyright (c) 2009-2015 Metaform Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Portions originally based on Apache Tuscany 2007
 * licensed under the Apache 2.0 license.
 */
package org.fabric3.api.binding.nats.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.fabric3.api.model.type.component.Binding;

/**
 * Binds a channel to a NATS cluster.
 */
public class NATSBinding extends Binding {
    private List<String> hosts = new ArrayList<>();
    private String defaultTopic;
    private String deserializer;
    private String serializer;

    // following values are defaults from the NATS client
    private boolean automaticReconnect = true;
    private long reconnectWaitTime = 2000;
    private boolean pedantic = false;
    private int maxFrameSize = 1048576;

    public NATSBinding() {
        super("nats", null, "binding.nats");
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public String getDeserializer() {
        return deserializer;
    }

    public String getSerializer() {
        return serializer;
    }

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

    void setDefaultTopic(String topic) {
        defaultTopic = topic;
    }

    void setTarget(URI uri) {
        targetUri = uri;
    }

    void setDeserializer(String deserializer) {
        this.deserializer = deserializer;
    }

    void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    void addHost(String host) {
        hosts.add(host);
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
}
