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
 */
package org.fabric3.binding.nats.monitor;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.Arrays;

import org.fabric3.api.host.runtime.HostInfo;
import org.fabric3.spi.introspection.IntrospectionContext;
import org.fabric3.spi.introspection.xml.AbstractValidatingTypeLoader;
import org.fabric3.spi.introspection.xml.LoaderRegistry;
import org.oasisopen.sca.Constants;
import org.oasisopen.sca.annotation.Destroy;
import org.oasisopen.sca.annotation.EagerInit;
import org.oasisopen.sca.annotation.Init;
import org.oasisopen.sca.annotation.Reference;

/**
 * Loads a {@link NATSAppenderDefinition}. The format is:
 *
 * <pre>
 *      appender.nats hosts="nats://localhost:4222" topic=".."
 * </pre>
 *
 * The hosts and topic attributes are optional. If hosts is not specified the default NATS address (localhost) will be used. If topic is not specified,
 * "fabric3" will be used; otherwise the topic may be a string or '#domain", which will set it to the value [domain].[runtime name].
 */
@EagerInit
public class NATSAppenderLoader extends AbstractValidatingTypeLoader<NATSAppenderDefinition> {
    private static final QName SCA_TYPE = new QName(Constants.SCA_NS, "appender.nats");
    private static final QName F3_TYPE = new QName(org.fabric3.api.Namespaces.F3, "appender.nats");

    private LoaderRegistry registry;
    private HostInfo hostInfo;

    public NATSAppenderLoader(@Reference LoaderRegistry registry, @Reference HostInfo hostInfo) {
        this.hostInfo = hostInfo;
        addAttributes("hosts", "topic");
        this.registry = registry;
    }

    @Init
    public void init() {
        // register under both namespaces
        registry.registerLoader(F3_TYPE, this);
        registry.registerLoader(SCA_TYPE, this);
    }

    @Destroy
    public void destroy() {
        registry.unregisterLoader(F3_TYPE);
        registry.unregisterLoader(SCA_TYPE);
    }

    public NATSAppenderDefinition load(XMLStreamReader reader, IntrospectionContext context) throws XMLStreamException {
        validateAttributes(reader, context);
        String value = reader.getAttributeValue(null, "hosts");
        String[] hosts = value == null ? new String[]{"nats://localhost:4222"} : value.split(",");
        for (int i = 0; i < hosts.length; i++) {
            if (!hosts[i].startsWith("nats://")) {
                hosts[i] = "nats://" + hosts[i];
            }
        }
        String topic = reader.getAttributeValue(null, "topic");
        if (topic == null) {
            topic = "fabric3";
        } else if ("#domain".equals(topic)) {
            topic = hostInfo.getDomain().getAuthority() + "." + hostInfo.getRuntimeName();
        }
        return new NATSAppenderDefinition(topic, Arrays.asList(hosts));
    }
}
