package com.openweb.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class BoatApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoatApplication.class, args);
    }

}
