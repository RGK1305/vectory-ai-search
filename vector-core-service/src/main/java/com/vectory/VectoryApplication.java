package com.vectory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class VectoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(VectoryApplication.class, args);
    }

    // This creates a shared "HTTP Client" bean that we can inject 
    // into our service to talk to the Python container.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}