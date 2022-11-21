package com.missakai.samplecameltesting.camel;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Route1Part1 extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:route1-part1")
                .routeId("route1-part1")
                .unmarshal().json(JsonLibrary.Jackson)
                // set some values for the current exchange
                .setProperty("OrderId",simple("${body[orderId]}"))
                // doing some preprocessing on the message
                .process(exchange -> {
                    log.info("Executing the preprocessor for order:{}",exchange.getProperty("OrderId"));
                    // your preprocessing logic. If you do not have any you can remove this .process() block
                })
                .log("Order consumed and preprocessed for order: ${body}")
                .to("direct:route1-part2")
                .end();
    }
}
