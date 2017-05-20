package com.github.corneil.data_rest_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.github.corneil.data_rest_demo"})
public class SpringDataRestAngularDemoApplication {
    public static void main(String[] args) {
        // Start the application
        SpringApplication.run(SpringDataRestAngularDemoApplication.class, args);
    }
}
