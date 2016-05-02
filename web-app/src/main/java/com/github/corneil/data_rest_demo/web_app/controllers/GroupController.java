package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.service.GroupDataInterface;
import com.github.corneil.data_rest_demo.web_app.service.UserDataInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by Corneil on 2016-05-01.
 */
@RestController
@RequestMapping(path = "/rest/groups")
public class GroupController extends AbstractRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    @Autowired
    protected GroupDataInterface groupData;
    @Autowired
    protected UserDataInterface userData;
    public GroupController() {
        super(logger);
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<Group>>> listAll() {
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
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource<Group>> load(@PathVariable String id) {
        Resource<Group> response = groupData.load(id);
        String resId = groupData.resourceId(response);
        Link selfRel = linkTo(GroupController.class).slash(resId).withSelfRel();
        Resource<Group> result = new Resource<Group>(response.getContent(), selfRel);
        Link groupOwnerRel = response.getLink("_groupOwner");
        String groupOwnerId = userData.resourceId(groupOwnerRel);
        Link groupOwner = linkTo(UserController.class).slash(groupOwnerId).withRel("groupOwner");
        result.getContent().setGroupOwner(groupOwner.getHref());
        result.add(groupOwner);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<Group>> create(@Valid Group group) {
        Assert.hasText(group.getGroupOwner());
        Link groupOwner = new Link(group.getGroupOwner(), "_groupOwner");
        String groupOwnerId = userData.resourceId(groupOwner);
        groupOwner = linkTo(UserController.class).slash(groupOwnerId).withRel("groupOwner");
        group.setGroupOwner(groupOwner.getHref());
        Resource<Group> response = groupData.create(group);
        String resId = groupData.resourceId(response);
        Link selfRel = linkTo(GroupController.class).slash(resId).withSelfRel();
        Resource<Group> result = new Resource<Group>(response.getContent(), selfRel);
        groupOwnerId = userData.resourceId(response.getLink("_groupOwner"));
        groupOwner = linkTo(UserController.class).slash(groupOwnerId).withRel("_groupOwner");
        result.getContent().setGroupOwner(groupOwner.getHref());
        result.add(groupOwner);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Resource<Group>> save(@Valid Group group) {
        Assert.hasText(group.getGroupOwner());
        Link groupOwner = new Link(group.getGroupOwner(), "_groupOwner");
        String groupOwnerId = userData.resourceId(groupOwner);
        groupOwner = linkTo(UserController.class).slash(groupOwnerId).withRel("groupOwner");
        group.setGroupOwner(groupOwner.getHref());
        groupData.save(group);
        Link selfRel = linkTo(GroupController.class).slash(groupOwnerId).withSelfRel();
        Resource<Group> response = new Resource<Group>(group, selfRel);
        String resId = groupData.resourceId(selfRel);
        Resource<Group> result = new Resource<Group>(response.getContent(), selfRel);
        groupOwnerId = userData.resourceId(response.getLink("_groupOwner"));
        groupOwner = linkTo(UserController.class).slash(groupOwnerId).withRel("_groupOwner");
        result.getContent().setGroupOwner(groupOwner.getHref());
        result.add(groupOwner);
        return ResponseEntity.ok(result);
    }
}
