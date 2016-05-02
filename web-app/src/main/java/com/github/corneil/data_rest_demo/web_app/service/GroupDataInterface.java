package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.dto.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * Created by Corneil on 2016-05-01.
 */
public interface GroupDataInterface {
    int countByGroupOwner(String userId);
    Resource<Group> create(Group user);
    void delete(String id);
    Resource<Group> find(String groupName);
    Resources<Resource<Group>> findAll();
    Resource<Group> load(String id);
    String resourceId(Link selfRel);
    String resourceId(Resource<Group> self);
    Resource<Group> save(String id, Resource<Group> user);
}
