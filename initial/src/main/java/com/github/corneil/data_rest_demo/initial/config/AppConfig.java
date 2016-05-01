package com.github.corneil.data_rest_demo.initial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@EnableJpaRepositories(basePackages = {"com.github.corneil.data_rest_demo.initial.repository"})
@ComponentScan(basePackages = {"com.github.corneil.data_rest_demo.initial"})
public class AppConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }
}
