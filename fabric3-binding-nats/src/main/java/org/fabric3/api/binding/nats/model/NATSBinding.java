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
import java.util.HashMap;
import java.util.Map;

import org.fabric3.api.model.type.component.Binding;

/**
 * Binds a channel to a NATS cluster.
 */
public class NATSBinding extends Binding {
    private Map<String, Object> configuration = new HashMap<>();
    private String defaultTopic;
    private String deserializer;
    private String serializer;

    public NATSBinding() {
        super("nats", null, "binding.nats");
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public String getDeserializer() {
        return deserializer;
    }

    public String getSerializer() {
        return serializer;
    }

    void setDefaultTopic(String topic) {
        defaultTopic = topic;
    }

    void addConfig(String key, String value) {
        configuration.put(key, value);
    }

    void setTarget(URI uri) {
        targetUri = uri;
    }

    public void setDeserializer(String deserializer) {
        this.deserializer = deserializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

}
