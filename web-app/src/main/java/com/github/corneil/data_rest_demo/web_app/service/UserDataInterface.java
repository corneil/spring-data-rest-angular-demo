package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import javax.jws.soap.SOAPBinding;

/**
 * Created by Corneil on 2016-05-01.
 */
public interface UserDataInterface {
    String resourceId(Resource<User>self);
    String resourceId(Link self);
    Resources<Resource<User>> findAll();
    Resources<Resource<User>> find(String input);
    Resource<User> load(String id);
    void delete(String id);
    Resource<User> create(User user);
}
