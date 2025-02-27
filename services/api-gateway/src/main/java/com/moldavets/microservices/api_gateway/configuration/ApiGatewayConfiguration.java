package com.moldavets.microservices.api_gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/report-generator-service/**")
                        .uri("lb://report-generator-service"))
                .route(p -> p.path("/job-parser-service/**")
                        .uri("lb://job-parser-service"))
                .build();
    }

}
