package com.github.corneil.data_rest_demo.initial.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Corneil on 2016/03/28.
 */
@Configuration
public class WebApplicationConfiguration extends WebMvcConfigurerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(WebApplicationConfiguration.class);
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/").setViewName("/index.html");
        logger.info("addViewControllers:{}", registry.toString());
    }
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurerAdapter() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.setBasePath("/api");
            }
        };
    }
}
