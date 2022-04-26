package com.webflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.domain.Greeting;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GreetingHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {

        log.info("method name: {}", request.methodName());
        log.info("attributes: {}", request.attributes());
        log.info("uri: {}", request.uri());

        return ServerResponse.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(new Greeting("Hello Spring!")));
    }
}
