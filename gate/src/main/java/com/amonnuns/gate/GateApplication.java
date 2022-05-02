package com.amonnuns.gate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class GateApplication {

    @Value("${DOORMAN_HOST:localhost}")
    private String doormanHost;

    @Value("${DOORMAN_PORT:8080}")
    private String doormanPort;

    private String doormanHostAndPort;

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        doormanHostAndPort ="http://"+doormanHost+":"+doormanPort;
        return builder
                .baseUrl(doormanHostAndPort)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GateApplication.class, args);
    }
}
