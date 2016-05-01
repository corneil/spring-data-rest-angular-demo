package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import static org.springframework.hateoas.client.Hop.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corneil on 2016-05-01.
 */
@RestController
@RequestMapping(path = "/rest/groups")
public class GroupController {
    private static final ParameterizedTypeReference<Resource<Group>> groupTypeRef = new ParameterizedTypeReference<Resource<Group>>() {};
    private static final ParameterizedTypeReference<Resources<Resource<Group>>> groupsTypeRef = new ParameterizedTypeReference<Resources<Resource<Group>>>() {};
    @Value("${data-service.url:http://localhost:8888}")
    protected String dataServiceUrl;
    @Autowired
    protected RestTemplate dataServiceClient;
    private Traverson traverson;
    private Traverson getTraverson() throws URISyntaxException {
        if (traverson == null) {
            traverson = new Traverson(new URI(dataServiceUrl), MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON);
        }
        return traverson;
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<Group>>> listAll() throws URISyntaxException {
        Resources<Resource<Group>> response = getTraverson().follow("groups").toObject(groupsTypeRef);
        List<Resource<Group>> content = new ArrayList<Resource<Group>>();
        for(Resource<Group> group : response.getContent()) {
            Link link = linkTo(GroupController.class).slash(group.getContent().getGroupName()).withSelfRel();
            group.getLink()
            content.add(new Resource<Group>(group, link));
        }
        return ResponseEntity.ok(new Resources<Resource<Group>>(content, linkTo(GroupController.class).withRel("groups")));
    }
}
