package org.swinchester;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by swinchester on 20/06/16.
 */
public class QueueToQueueRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("activemq:queue:jms-xa-demo.in")
                .transacted("requiredJta")
                .log("Received msg with JMSRedelivered:${header.JMSRedelivered}")
                .processRef("testProcessor")
                .to("activemq-2:queue:jms-xa-demo.out");
    }
}
