package com.webflux.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.handler.GreetingHandler;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> routeGet(GreetingHandler greetingHandler) {
        return RouterFunctions.route(GET("/hello").and(accept(MediaType.APPLICATION_JSON)),
                                     greetingHandler::hello);
    }

    @Bean
    public RouterFunction<ServerResponse> routePost(GreetingHandler greetingHandler) {
        return RouterFunctions.route(POST("/hello").and(accept(MediaType.APPLICATION_JSON)),
                                     greetingHandler::hello);
    }

    @Bean
    public RouterFunction<ServerResponse> unauthorized(GreetingHandler greetingHandler) {
        return RouterFunctions.route(POST("/hello/unauthorized").and(accept(MediaType.APPLICATION_JSON)),
                                     greetingHandler::unauthorized);
    }

    @Bean
    public RouterFunction<ServerResponse> conflict(GreetingHandler greetingHandler) {
        return RouterFunctions.route(POST("/hello/conflict").and(accept(MediaType.APPLICATION_JSON)),
                                     greetingHandler::conflict);
    }

    @Bean
    public RouterFunction<ServerResponse> defaultException(GreetingHandler greetingHandler) {
        return RouterFunctions.route(POST("/hello/default").and(accept(MediaType.APPLICATION_JSON)),
                                     greetingHandler::defaultException);
    }
}
