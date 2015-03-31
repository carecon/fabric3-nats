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
package org.fabric3.binding.nats.introspection;

import nats.client.Nats;
import nats.client.Subscription;
import org.fabric3.api.binding.nats.model.NATSBinding;
import org.fabric3.api.model.type.component.Channel;
import org.fabric3.spi.introspection.dsl.ChannelIntrospector;
import org.oasisopen.sca.annotation.EagerInit;

/**
 * Adds connection types to NATS binding configuration.
 *
 * Adding the types as opposed to referencing them directly in {@link NATSBinding} avoids requiring user code to have a dependency on NATS libraries.
 */
@EagerInit
public class NATSIntrospector implements ChannelIntrospector {
    public void introspect(Channel channel) {
        channel.getBindings().forEach(b -> {
            if (b instanceof NATSBinding) {
                b.setConnectionTypes(Nats.class, Subscription.class);
            }
        });

    }
}
