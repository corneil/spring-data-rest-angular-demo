package com.github.corneil.data_rest_demo.web_app.service;

import static org.springframework.hateoas.client.Hop.rel;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.corneil.data_rest_demo.web_app.dto.User;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;

/**
 * Created by Corneil on 2016-05-01.
 */
@Service("userDataService")
public class UserDataService extends AbstractDataService implements UserDataInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);
    private static ParameterizedTypeReference<Resources<Resource<User>>> usersTypeRef = new ParameterizedTypeReference<Resources<Resource<User>>>() {
    };
    private static ParameterizedTypeReference<Resource<User>> userTypeRef = new ParameterizedTypeReference<Resource<User>>() {
    };

    @Override
    public Resource<User> create(@Valid User user) {
        Assert.notNull(user);
        Link usersRel = getTraverson().follow("users").asLink();
        HttpEntity<User> request = new HttpEntity<User>(user);
        ResponseEntity<Resource<User>> response = dataServiceClient.exchange(usersRel.getHref(), HttpMethod.POST, request, userTypeRef);
        // TODO status
        return response.getBody();
    }

    @Override
    public void delete(String id) {
        Assert.notNull(id);
        try {
            String url = getTraverson().follow("users").asLink().getHref();
            dataServiceClient.delete(new URI(url + "/" + id));
        } catch (URISyntaxException x) {
            throw new RuntimeException("resourceId:exception:" + x, x);
        }
    }

    @Override
    public Resources<Resource<User>> find(String input) {
        Assert.notNull(input);
        return getTraverson().follow("users").follow(rel("search")).follow(rel("findLikeUserIdOrFullName").withParameter("input", input))
                .toObject(usersTypeRef);
    }

    @Override
    public Resources<Resource<User>> findAll() {
        return getTraverson().follow("users").toObject(usersTypeRef);
    }

    @Override
    public Resource<User> load(String id) {
        Assert.notNull(id);
        String url = getTraverson().follow("users").asLink().getHref();
        ResponseEntity<Resource<User>> response = dataServiceClient.exchange(url + "/" + id, HttpMethod.GET, null, userTypeRef);
        return response.getBody();
    }
    @Override
    public String resourceLink(String id) {
        return getTraverson().follow("users").asLink().getHref() + "/" + id;
    }
    @Override
    public String resourceId(Link self) {
        Assert.notNull(self);
        return RestHelper.resourceId(self.getHref(), getTraverson().follow("users").asLink().getHref());
    }

    @Override
    public String resourceId(Resource<User> self) {
        return resourceId(self.getLink("self"));
    }

    @Override
    public Resource<User> save(String id, Resource<User> user) {
        Assert.notNull(user);
        String url = getTraverson().follow("users").asLink().getHref();
        HttpEntity<Resource<User>> request = new HttpEntity<Resource<User>>(user);
        ResponseEntity<Resource<User>> response = dataServiceClient.exchange(url + "/" + id, HttpMethod.PUT, request, userTypeRef);
        // TODO check status code
        return response.getBody();
    }
}
