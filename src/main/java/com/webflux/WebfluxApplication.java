package com.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.webflux.webclient.GreetingClient;
import com.webflux.webclient.LineClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class WebfluxApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(WebfluxApplication.class, args);
        GreetingClient greetingClient = context.getBean(GreetingClient.class);
        // We need to block for the content here or the JVM might exit before the message is logged
        System.out.println(">> message = " + greetingClient.getMessage().block());

        LineClient lineClient = context.getBean(LineClient.class);

    }
}
