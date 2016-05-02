package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.service.GroupDataInterface;
import com.github.corneil.data_rest_demo.web_app.service.UserDataInterface;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<Group>> create(@RequestBody Group group) {
        logger.debug("create:{}", group);
        Assert.hasText(group.getGroupOwner(), "groupOwner required");
        String groupOwnerId = RestHelper.resourceId(group.getGroupOwner(), linkTo(UserController.class).toUri().toString());
        group.setGroupOwner(userData.resourceLink(groupOwnerId));
        Resource<Group> response = groupData.create(group);
        Resource<Group> result = createGroupResource(response);
        return ResponseEntity.ok(result);
    }
    private Resource<Group> createGroupResource(Resource<Group> item) {
        String resId = groupData.resourceId(item);
        Link selfRel = linkTo(GroupController.class).slash(resId).withSelfRel();
        Resource<Group> result = new Resource<Group>(item.getContent(), selfRel);
        String groupOwnerId = userData.resourceId(item.getLink("_groupOwner"));
        Link groupOwner = linkTo(UserController.class).slash(groupOwnerId).withRel("groupOwner");
        result.getContent().setGroupOwner(groupOwner.getHref());
        result.add(groupOwner);

        return result;
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<Group>>> findAll() {
        logger.debug("findAll");
        Resources<Resource<Group>> response = groupData.findAll();
        List<Resource<Group>> content = new ArrayList<Resource<Group>>();
        for (Resource<Group> group : response.getContent()) {
            content.add(createGroupResource(group));
        }
        return ResponseEntity.ok(new Resources<Resource<Group>>(content, linkTo(GroupController.class).withRel("groups")));
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource<Group>> load(@PathVariable String id) {
        logger.debug("load:{}", id);
        Resource<Group> response = groupData.load(id);
        return ResponseEntity.ok(createGroupResource(response));
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Resource<Group>> save(@PathVariable String id, @RequestBody Resource<Group> group) {
        logger.debug("save:{},{}", id, group);
        Assert.hasText(group.getContent().getGroupOwner());
        String groupOwnerId = RestHelper.resourceId(group.getContent().getGroupOwner(), linkTo(UserController.class).toUri().toString());
        group.getContent().setGroupOwner(userData.resourceLink(groupOwnerId));
        Resource<Group> result = createGroupResource(groupData.save(id, group));
        return ResponseEntity.ok(result);
    }
}
