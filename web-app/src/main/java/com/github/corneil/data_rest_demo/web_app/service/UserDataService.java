package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.User;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;
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
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.hateoas.client.Hop.*;

/**
 * Created by Corneil on 2016-05-01.
 */
@Service("userDataService")
public class UserDataService implements UserDataInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);
    private static ParameterizedTypeReference<Resource<User>> userTypeRef = new ParameterizedTypeReference<Resource<User>>() {
    };
    private static ParameterizedTypeReference<Resources<Resource<User>>>
            usersTypeRef =
            new ParameterizedTypeReference<Resources<Resource<User>>>() {
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
    public String resourceId(Resource<User> self) {
        return resourceId(self.getLink("self"));
    }
    @Override
    public String resourceId(Link self) {
        Assert.notNull(self);
        return RestHelper.resourceId(self.getHref(), getTraverson().follow("users").asLink().getHref());
    }
    @Override
    public Resources<Resource<User>> findAll() {
        return getTraverson().follow("users").toObject(usersTypeRef);
    }
    @Override
    public Resources<Resource<User>> find(String input) {
        Assert.notNull(input);
        return getTraverson().follow("users")
                             .follow(rel("search"))
                             .follow(rel("findLikeUserIdOrFullName").withParameter("input", input))
                             .toObject(usersTypeRef);
    }
    @Override
    public Resource<User> load(String id) {
        Assert.notNull(id);
        Link usersRel = getTraverson().follow("users").asLink();
        Resource<User> user = dataServiceClient.getForObject(usersRel.getHref() + "/" + id, User.UserResource.class);
        return user;
    }
    @Override
    public void delete(String id) {
        Assert.notNull(id);
        try {
            Link selfRel = getTraverson().follow("users").follow(id).asLink();
            dataServiceClient.delete(new URI(selfRel.getHref()));
        } catch (URISyntaxException x) {
            throw new RuntimeException("resourceId:exception:" + x, x);
        }
    }
    @Override
    public Resource<User> create(@Valid User user) {
        Assert.notNull(user);
        Link usersRel = getTraverson().follow("users").asLink();
        return dataServiceClient.postForObject(usersRel.getHref(), user, User.UserResource.class);
    }
}
