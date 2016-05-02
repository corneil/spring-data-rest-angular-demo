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
    Resources<Resource<Group>> findAll();
    Resource<Group> load(String id);
    Resource<Group> find(String groupName);
    String resourceId(Link selfRel);
    String resourceId(Resource<Group> self);
    void save(Group user);
}
