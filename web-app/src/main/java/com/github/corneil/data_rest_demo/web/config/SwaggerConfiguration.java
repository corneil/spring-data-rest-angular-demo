package com.github.corneil.data_rest_demo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Corneil du Plessis
 */
@EnableSwagger2
@Configuration
//@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerConfiguration {
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
    }

	private ApiInfo apiInfo() {
		return new ApiInfo("Spring Data Rest Demo",
				"Demonstrate Spring Data RestController",
				"2",
				"/",
				new Contact("Corneil du Plessis", "","corneil.duplessis@gmail.com"),
				"",
				"");
	}
}
