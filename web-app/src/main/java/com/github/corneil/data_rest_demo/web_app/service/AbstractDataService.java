package com.github.corneil.data_rest_demo.web_app.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractDataService {

    @Autowired
    protected RestTemplate dataServiceClient;
    @Value("${data-service.url:http://localhost:8888}")
    protected String dataServiceUrl;
    private Traverson traverson;

    protected Traverson getTraverson() {
        try {
            if (traverson == null) {
                traverson = new Traverson(new URI(dataServiceUrl), MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON);
            }
            return traverson;
        } catch (URISyntaxException x) {
            throw new RuntimeException("resourceId:exception:" + x, x);
        }
    }

}
