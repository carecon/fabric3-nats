Fabric3 NATS Binding
=====================

This repository hosts support for NATS-based channels. Information on Fabric3 can be found at http://www.fabric3.org.


Building the Source
------------------------

Requirements are JDK 8 and Gradle 1.11+.

To build the source, execute:

./gradlew

See build.gradle for additional instructions

Usage
-------------------------

See docs.fabric3.org or test-nats for examples of using the NATS binding.

The NATS monitor appender is configured as follows in the Fabric3 systemConfig.xml:

<config ...>
   <monitor>
      <appenders>
         <appender.nats hosts="localhost:4222" topic="#domain"/>
      </appenders>
   </monitor>
</config>

The 'hosts' attribute is a comma-separated list of NATS broker addresses (if not specified, the default NATS localhost address will be used). The hosts
attribute can also accept environment variables in the form: ${var1}. The JVM properties will first be searched followed by OS environment variables.

The 'topic' attribute is optional. If it is not set, the topic will default to 'fabric3'. If it is set to '#domain' the topic will be the the Fabric3 domain
name appended with the runtime name as in 'domain.vm'.


The NATS binding supports extensible de/serializers. A de/serializer is a component that implements java.util.Function and take or return a String (the message
payload type supported by NATS). De/serializers are configured on the NATS Binding by setting the component name. For an example, see JsonSerializer and
JsonDeserializer in the test-nats module.

License
-------------------------
Licensed under the Apache 2.0 License (http://fabric3.org/license.html)
