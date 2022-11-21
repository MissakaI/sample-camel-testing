package com.missakai.samplecameltesting.camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Route1Part2 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:route1-part2").routeId("route1-part2")
                .marshal().json(JsonLibrary.Jackson)
                // doing some preprocessing on the message
                .process(exchange -> {
                    log.info("Executing the order processor for order:{}",exchange.getProperty("OrderId"));
                    // your processing logic. If you do not have any you can remove this .process() block
                })
                .log("Order processed for order: ${body}")
                .to("direct:route1-end")
                .end();
    }
}
