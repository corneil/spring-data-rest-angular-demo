package com.github.corneil.data_rest_demo.web.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Corneil on 2016-04-30.
 */
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfo("Spring Data Rest Demo",
                "Demonstrate Spring Data RestController",
                "2",
                "/",
                "corneil.duplessis@gmail.com",
                "",
                ""));
    }
}
