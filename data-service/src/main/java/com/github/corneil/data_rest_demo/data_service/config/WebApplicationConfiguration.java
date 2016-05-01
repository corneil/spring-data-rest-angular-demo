package com.github.corneil.data_rest_demo.initial.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Corneil on 2016/03/28.
 */
@Configuration
public class WebApplicationConfiguration extends WebMvcConfigurerAdapter {
    private final static Logger logger = LoggerFactory.getLogger(WebApplicationConfiguration.class);
}
