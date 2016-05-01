package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.dto.User;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * Created by Corneil on 2016-05-01.
 */
public interface GroupDataInterface {
    public String resourceId(Resource<Group> self);
    Resources<Resource<Group>> findAll();
}
