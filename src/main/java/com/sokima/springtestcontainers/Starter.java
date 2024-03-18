package com.sokima.springtestcontainers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @RestController
    record GreetingController() {

        @GetMapping("greet")
        public String greet() {
            return "greetings: " + Math.random();
        }
    }
}
