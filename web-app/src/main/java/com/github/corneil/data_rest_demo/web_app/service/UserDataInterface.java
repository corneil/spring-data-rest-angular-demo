package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * Created by Corneil on 2016-05-01.
 */
public interface UserDataInterface {
    Resource<User> create(User user);
    void delete(String id);
    Resources<Resource<User>> find(String input);
    Resources<Resource<User>> findAll();
    Resource<User> load(String id);
    String resourceId(Link self);
    String resourceId(Resource<User> self);
    Resource<User> save(String id, Resource<User> user);
}
