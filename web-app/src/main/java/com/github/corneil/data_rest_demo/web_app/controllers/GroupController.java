package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.service.GroupDataInterface;
import com.github.corneil.data_rest_demo.web_app.service.UserDataInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by Corneil on 2016-05-01.
 */
@RestController
@RequestMapping(path = "/rest/groups")
public class GroupController {
    @Autowired
    protected GroupDataInterface groupData;
    @Autowired
    protected UserDataInterface userData;
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<Group>>> listAll() throws URISyntaxException {
        Resources<Resource<Group>> response = groupData.findAll();
        List<Resource<Group>> content = new ArrayList<Resource<Group>>();
        for (Resource<Group> group : response.getContent()) {
            String id = groupData.resourceId(group);
            Link self = linkTo(GroupController.class).slash(id).withSelfRel();
            Resource<Group> item = new Resource<Group>(group.getContent(), self);
            Link groupOwner = group.getLink("_groupOwner");
            Assert.notNull(groupOwner, "expected _groupOwner");
            String resourceId = userData.resourceId(groupOwner);
            item.add(linkTo(UserController.class).slash(resourceId).withRel("groupOwner"));
            content.add(item);
        }
        return ResponseEntity.ok(new Resources<Resource<Group>>(content, linkTo(GroupController.class).withRel("groups")));
    }
}
