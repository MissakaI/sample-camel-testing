package com.missakai.samplecameltesting.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(
        properties = {
                // Disable other routes being initialized for testing only this route
                "camel.springboot.java-routes-include-pattern=**/Route1Part1*"
        }
)
@Slf4j
class Route1Part1Test {

    @Autowired
    CamelContext camelContext;

    @Produce("direct:route1-part1")
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:direct:route1-part2")
    MockEndpoint directPart2MockEndpoint;

    static ObjectMapper objectMapper=new ObjectMapper();

    @Test
    void testRoute() throws Exception {
        // mock all endpoints
        AdviceWith.adviceWith(camelContext, "route1-part1", AdviceWithRouteBuilder::mockEndpoints);

        directPart2MockEndpoint = camelContext.getEndpoint("mock:direct:route1-part2",MockEndpoint.class);

        directPart2MockEndpoint.setExpectedMessageCount(1);

        producerTemplate.sendBody(
                "direct:route1-part1",
                objectMapper.writeValueAsString(Map.of("orderId","ABC0001"))
        );

        directPart2MockEndpoint.assertIsSatisfied();
    }

}
