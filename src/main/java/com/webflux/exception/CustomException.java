package com.webflux.exception;

import java.util.Arrays;

import org.springframework.http.HttpHeaders;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private int value;
    private String reasonPhrase;
    private HttpHeaders httpHeaders;
    private byte[] bytes;

    public CustomException(int value, String reasonPhrase, HttpHeaders asHttpHeaders,
                           byte[] bytes) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
        this.httpHeaders = asHttpHeaders;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "CustomException{" +
               "value=" + value +
               ", reasonPhrase='" + reasonPhrase + '\'' +
               ", httpHeaders=" + httpHeaders +
               ", bytes=" + Arrays.toString(bytes) +
               '}';
    }
}
