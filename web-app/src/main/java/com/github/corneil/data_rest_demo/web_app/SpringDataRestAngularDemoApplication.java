package com.github.corneil.data_rest_demo.web_app;

import com.github.corneil.data_rest_demo.common.filter.RequestLoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringDataRestAngularDemoApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringDataRestAngularDemoApplication.class);
    }
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new RequestLoggingFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/rest/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
    public static void main(String[] args) {
        // Start the application
        SpringApplication.run(SpringDataRestAngularDemoApplication.class, args);
    }
}
