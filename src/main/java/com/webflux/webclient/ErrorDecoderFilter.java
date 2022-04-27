package com.webflux.webclient;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import com.webflux.exception.CustomException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class ErrorDecoderFilter {
    public static ExchangeFilterFunction errorDecoderFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            final HttpStatus httpStatus = clientResponse.statusCode();
            if (httpStatus == HttpStatus.UNAUTHORIZED) {
                final String token = UUID.randomUUID().toString();
                log.warn("401 status, token=" + token);
            }
            if (httpStatus == HttpStatus.CONFLICT) {
                return clientResponse.bodyToMono(String.class).flatMap(
                    body ->
                        Mono.error(new CustomException(HttpStatus.CONFLICT.value(),
                                                       HttpStatus.CONFLICT.getReasonPhrase(),
                                                       clientResponse.headers()
                                                                     .asHttpHeaders(),
                                                       body.getBytes(
                                                           StandardCharsets.UTF_8))));
            }
            if (!httpStatus.is2xxSuccessful()) {
                return clientResponse.bodyToMono(String.class).flatMap(
                    body ->
                        Mono.error(new CustomException(httpStatus.value(),
                                                       httpStatus.getReasonPhrase(),
                                                       clientResponse.headers()
                                                                     .asHttpHeaders(),
                                                       body.getBytes(
                                                           StandardCharsets.UTF_8))));
            }
            return Mono.justOrEmpty(clientResponse);
        });
    }
}
