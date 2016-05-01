package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.hateoas.client.Hop.*;

/**
 * Created by Corneil on 2016-05-01.
 */
@Service("groupDataService")
public class GroupDataService implements GroupDataInterface {
    private static final Logger logger = LoggerFactory.getLogger(GroupDataService.class);
    private static ParameterizedTypeReference<Resources<Resource<Group>>>
            groupsTypeRef =
            new ParameterizedTypeReference<Resources<Resource<Group>>>() {
            };
    private static ParameterizedTypeReference<Resource<Group>> groupTypeRef = new ParameterizedTypeReference<Resource<Group>>() {
    };
    @Autowired
    protected RestTemplate dataServiceClient;
    @Value("${data-service.url:http://localhost:8888}")
    protected String dataServiceUrl;
    private Traverson traverson;
    private Traverson getTraverson() {
        try {
            if (traverson == null) {
                traverson = new Traverson(new URI(dataServiceUrl), MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON);
            }
            return traverson;
        } catch (URISyntaxException x) {
            throw new RuntimeException("resourceId:exception:" + x, x);
        }
    }
    @Override
    public String resourceId(Resource<Group> self) {
        return RestHelper.resourceId(self.getLink("self").getHref(), getTraverson().follow("groups").asLink().getHref());
    }
    @Override
    public Resources<Resource<Group>> findAll() {
        return getTraverson().follow("groups").toObject(groupsTypeRef);
    }
    @Override
    public int countByGroupOwner(String userId) {
        String response =
                getTraverson().follow("groups").follow("search")
                              .follow(rel("countByGroupOwner_UserId").withParameter("userId", userId))
                              .toObject(String.class);
        return Integer.parseInt(response);
    }
}
