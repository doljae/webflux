package com.webflux.webclient;

import java.util.UUID;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class AuthorizationFilter {
    public static ExchangeFilterFunction authorizationFilter() {
        return (request, next) -> {
            final String accessToken = UUID.randomUUID().toString();
            final ClientRequest newRequest =
                ClientRequest.from(request).header("Authorization", String.format("Bearer %s", accessToken))
                             .build();
            return next.exchange(newRequest);
        };
    }
}
