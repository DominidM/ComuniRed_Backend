package com.comunired.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.graphql.server.webmvc.GraphQlWebSocketHandler;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.accept;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class GraphQLMultipartConfig {
    
    @Bean
    public RouterFunction<ServerResponse> graphQlMultipartRouterFunction(GraphQlHttpHandler handler) {
        return route()
            .POST("/graphql", accept(org.springframework.http.MediaType.MULTIPART_FORM_DATA), handler::handleRequest)
            .build();
    }
}
