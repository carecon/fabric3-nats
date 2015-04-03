package f3;

import javax.xml.namespace.QName;

import org.fabric3.api.Namespaces;
import org.fabric3.api.annotation.model.Provides;
import org.fabric3.api.model.type.builder.CompositeBuilder;
import org.fabric3.api.model.type.component.Composite;
import org.fabric3.binding.nats.generator.NATSConnectionBindingGenerator;
import org.fabric3.binding.nats.introspection.NATSIntrospector;
import org.fabric3.binding.nats.monitor.NATSAppenderBuilder;
import org.fabric3.binding.nats.monitor.NATSAppenderGenerator;
import org.fabric3.binding.nats.monitor.NATSAppenderLoader;
import org.fabric3.binding.nats.runtime.NATSConnectionManagerImpl;
import org.fabric3.binding.nats.runtime.NATSConnectionSourceAttacher;
import org.fabric3.binding.nats.runtime.NATSConnectionTargetAttacher;
import org.fabric3.spi.model.type.system.SystemComponentBuilder;

/**
 *
 */
public class NATSProvider {
    private static final QName QNAME = new QName(Namespaces.F3, "NATSExtension");

    @Provides
    public static Composite getComposite() {
        CompositeBuilder compositeBuilder = CompositeBuilder.newBuilder(QNAME);

        // setup monitoring
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSAppenderLoader.class).build());
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSAppenderGenerator.class).build());
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSAppenderBuilder.class).build());

        // setup binding infrastructure
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSIntrospector.class).build());
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSConnectionBindingGenerator.class).build());
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSConnectionSourceAttacher.class).build());
        compositeBuilder.component(SystemComponentBuilder.newBuilder(NATSConnectionTargetAttacher.class).build());

        SystemComponentBuilder managerBuilder = SystemComponentBuilder.newBuilder(NATSConnectionManagerImpl.class);
        managerBuilder.reference("executorService", "RuntimeThreadPoolExecutor");
        compositeBuilder.component(managerBuilder.build());

        compositeBuilder.deployable();
        return compositeBuilder.build();
    }
}
