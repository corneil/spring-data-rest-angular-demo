package com.github.corneil.data_rest_demo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author Corneil du Plessis
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.github.corneil.data_rest_demo.web.repository"})
@Import({WebApplicationConfiguration.class, SwaggerConfiguration.class})
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
