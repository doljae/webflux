package com.webflux.webclient;

import static com.webflux.webclient.AuthorizationFilter.authorizationFilter;
import static com.webflux.webclient.ErrorDecoderFilter.errorDecoderFilter;

import java.util.UUID;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.webflux.domain.Greeting;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Component
public class LineClient {

    private final WebClient webClient;

    public LineClient(WebClient.Builder builder) {
        final var httpClient = HttpClient.create();
        httpClient.warmup().block();

        webClient = builder
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            // .baseUrl("http://localhost:1234")
            .filter(authorizationFilter())
            .filter(errorDecoderFilter())
            .build();
    }

    public Mono<String> getMessage(String uri) {
        return webClient.post()
                        .uri(uri)
                        .header("test-key", UUID.randomUUID().toString())
                        .bodyValue(new Greeting("dummy message"))
                        .retrieve()
                        .bodyToMono(String.class);
    }
}
