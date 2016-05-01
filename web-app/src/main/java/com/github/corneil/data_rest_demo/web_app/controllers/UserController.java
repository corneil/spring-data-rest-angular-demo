package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.client.Hop.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by Corneil on 2016-04-30.
 */
@RestController
@RequestMapping("/rest/users")
public class UserController extends AbstractRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static ParameterizedTypeReference<Resources<User>> usersTypeRef = new ParameterizedTypeReference<Resources<User>>() {
    };
    private static ParameterizedTypeReference<Resource<User>> userTypeRef = new ParameterizedTypeReference<Resource<User>>() {
    };
    @Value("${data-service.url:http://localhost:8888}")
    protected String dataServiceUrl;
    private Traverson traverson;
    private Traverson getTraverson() throws URISyntaxException {
        if (traverson == null) {
            traverson = new Traverson(new URI(dataServiceUrl), MediaTypes.HAL_JSON, MediaType.APPLICATION_JSON);
        }
        return traverson;
    }
    public UserController() {
        super(logger);
    }
    @Autowired
    protected RestTemplate dataServiceClient;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<User>>> listAll() throws URISyntaxException {
        Resources<User> response = getTraverson().follow("users").toObject(usersTypeRef);
        logger.debug("response:{}", response);
        List<Resource<User>> content = new ArrayList<Resource<User>>();
        for (User user : response.getContent()) {
            Link selfRel = linkTo(UserController.class).slash(user.getUserId()).withSelfRel();
            content.add(new Resource<User>(user, selfRel));
        }
        Resources<Resource<User>> result = new Resources<Resource<User>>(content, linkTo(UserController.class).withRel("users"));
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/find/{input}", method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<User>>> find(@PathVariable String input) throws URISyntaxException {
        Resources<User>
                response =
                getTraverson().follow("users")
                              .follow(rel("search"))
                              .follow(rel("findLikeUserIdOrFullName").withParameter("input", input))
                              .toObject(usersTypeRef);
        logger.debug("response:{}", response);
        List<Resource<User>> content = new ArrayList<Resource<User>>();
        Link link = linkTo(UserController.class).withRel("users");
        for (User user : response.getContent()) {
            Resource<User> item = new Resource<User>(user, linkTo(UserController.class).slash(user.getUserId()).withSelfRel());
            content.add(item);
        }
        Resources<Resource<User>> result = new Resources<Resource<User>>(content, link);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<User>> add(@RequestBody User user) throws URISyntaxException {
        String url = UriComponentsBuilder.fromHttpUrl(dataServiceUrl).path("/users").toUriString();
        logger.debug("url={}", url);
        User response = dataServiceClient.postForObject(url, user, User.class);
        Resource<User> result = new Resource<User>(response, linkTo(UserController.class).slash(response.getUserId()).withSelfRel());
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String userId) throws URISyntaxException {
        // TODO determine if group owner
        // TODO delete group memberships
        Resource<User>
                user =
                getTraverson().follow("users", "search")
                              .follow(rel("findOneByUserId").withParameter("userId", userId))
                              .toObject(userTypeRef);
        String url = user.getLink("self").getHref();
        logger.debug("url={}", url);
        dataServiceClient.delete(url);
        return ResponseEntity.ok(null);
    }
    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Resource<User>> load(@PathVariable String userId) throws URISyntaxException {
        User
                user =
                getTraverson().follow("users", "search")
                              .follow(rel("findOneByUserId").withParameter("userId", userId))
                              .toObject(User.class);
        Resource<User> result = new Resource<User>(user, linkTo(UserController.class).slash(user.getUserId()).withSelfRel());
        return ResponseEntity.ok(result);
    }
}
