package org.fabric3.binding.nats.runtime;

import java.net.URI;
import java.util.function.Function;

import org.fabric3.api.host.Fabric3Exception;
import org.fabric3.api.host.runtime.HostInfo;
import org.fabric3.spi.container.component.Component;
import org.fabric3.spi.container.component.ComponentManager;
import org.fabric3.spi.container.component.ScopedComponent;

/**
 *
 */
public class InstanceResolver {

    public static Function getInstance(String name, HostInfo info, ComponentManager cm) {
        URI serializerUri = URI.create(info.getDomain().toString() + "/" + name);
        Component component = cm.getComponent(serializerUri);
        if (component == null) {
            throw new Fabric3Exception("Component not found: " + name);
        }
        if (!(component instanceof ScopedComponent)) {
            throw new Fabric3Exception("Component must be a Java component: " + name);
        }
        ScopedComponent scopedComponent = (ScopedComponent) component;
        Object instance = scopedComponent.getInstance();
        if (!(instance instanceof Function)) {
            throw new Fabric3Exception("Serializer must implement: " + Function.class.getName());
        }
        return (Function) instance;
    }

    private InstanceResolver() {
    }
}
