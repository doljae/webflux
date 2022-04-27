package com.webflux;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.webflux.exception.CustomException;
import com.webflux.webclient.LineClient;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class LineClientTest {
    @Autowired
    private LineClient lineClient;
    private MockWebServer mockBackEnd;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        mockBackEnd = new MockWebServer();
        baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void test() {
        setLineBotApiResponseStatus(OK);
        assertDoesNotThrow(() -> lineClient.getMessage(baseUrl).block());
    }

    @Test
    void unauthorized() {
        setLineBotApiResponseStatus(UNAUTHORIZED);
        assertThrows(CustomException.class, () -> lineClient.getMessage(baseUrl).block());
    }

    @Test
    void conflict() {
        setLineBotApiResponseStatus(CONFLICT);
        assertThrows(CustomException.class, () -> lineClient.getMessage(baseUrl).block());
    }

    @Test
    void defaultException() {
        setLineBotApiResponseStatus(INTERNAL_SERVER_ERROR);
        assertThrows(CustomException.class, () -> lineClient.getMessage(baseUrl).block());
    }

    private void setLineBotApiResponseStatus(HttpStatus httpStatus) {
        mockBackEnd.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {
                if (httpStatus == UNAUTHORIZED) {
                    return new MockResponse().setResponseCode(UNAUTHORIZED.value())
                                             .setBody(UNAUTHORIZED.getReasonPhrase());
                }
                if (httpStatus == CONFLICT) {
                    return new MockResponse().setResponseCode(CONFLICT.value())
                                             .setBody(CONFLICT.getReasonPhrase());
                }
                if (!httpStatus.is2xxSuccessful()) {
                    return new MockResponse().setResponseCode(INTERNAL_SERVER_ERROR.value())
                                             .setBody(INTERNAL_SERVER_ERROR.getReasonPhrase());
                }
                return new MockResponse().setResponseCode(200).setBody(OK.getReasonPhrase());
            }
        });
    }
}
