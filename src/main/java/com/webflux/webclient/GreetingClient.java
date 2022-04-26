package com.webflux.webclient;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.webflux.domain.Greeting;

import reactor.core.publisher.Mono;

@Component
public class GreetingClient {
    private final WebClient webClient;

    public GreetingClient(WebClient.Builder builder) {
        webClient = builder.baseUrl("http://localhost:1234").build();
    }

    public Mono<String> getMessage() {
        return webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(Greeting.class)
                        .map(Greeting::getMessage);
    }
}
