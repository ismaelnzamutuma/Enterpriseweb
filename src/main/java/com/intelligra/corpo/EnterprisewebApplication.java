package com.intelligra.corpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class EnterprisewebApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnterprisewebApplication.class, args);
    }



    @Bean

    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate;
    }

}
