package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.ErrorMessage;
import com.github.corneil.data_rest_demo.web_app.dto.User;
import com.github.corneil.data_rest_demo.web_app.service.GroupDataInterface;
import com.github.corneil.data_rest_demo.web_app.service.UserDataInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by Corneil on 2016-04-30.
 */
@RestController
@RequestMapping("/rest/users")
public class UserController extends AbstractRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    protected UserDataInterface userData;
    @Autowired
    protected GroupDataInterface groupData;
    public UserController() {
        super(logger);
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<User>>> listAll() throws URISyntaxException {
        Resources<Resource<User>> response = userData.findAll();
        List<Resource<User>> content = new ArrayList<Resource<User>>();
        for (Resource<User> user : response.getContent()) {
            String id = userData.resourceId(user);
            Link selfRel = linkTo(UserController.class).slash(id).withSelfRel();
            content.add(new Resource<User>(user.getContent(), selfRel));
        }
        Link usersRel = linkTo(UserController.class).withRel("users");
        Resources<Resource<User>> result = new Resources<>(content, usersRel);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/find/{input}", method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<User>>> find(@PathVariable String input) throws URISyntaxException {
        Resources<Resource<User>> response = userData.find(input);
        logger.debug("response:{}", response);
        List<Resource<User>> content = new ArrayList<Resource<User>>();
        Link link = linkTo(UserController.class).withRel("users");
        for (Resource<User> user : response.getContent()) {
            String id = userData.resourceId(user);
            Resource<User> item = new Resource<User>(user.getContent(), linkTo(UserController.class).slash(id).withSelfRel());
            content.add(item);
        }
        Resources<Resource<User>> result = new Resources<Resource<User>>(content, link);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<User>> add(@RequestBody User user) throws URISyntaxException {
        Resource<User> response = userData.create(user);
        String id = userData.resourceId(response);
        Resource<User> result = new Resource<User>(response.getContent(), linkTo(UserController.class).slash(id).withSelfRel());
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Resource<User>> save(@PathVariable String id, @RequestBody Resource<User> user) throws URISyntaxException {
        Resource<User> response = userData.save(id, user);
        String resId = userData.resourceId(response);
        Resource<User> result = new Resource<User>(response.getContent(), linkTo(UserController.class).slash(resId).withSelfRel());
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id) throws URISyntaxException {
        Resource<User> user = userData.load(id);
        int count = groupData.countByGroupOwner(user.getContent().getUserId());
        if (count > 0) {
            ErrorMessage msg = new ErrorMessage(String.format("%s owns %d groups", user.getContent().getUserId(), count));
            return ResponseEntity.badRequest().body(msg);
        }
        // TODO delete group memberships
        userData.delete(id);
        return ResponseEntity.ok(null);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource<User>> load(@PathVariable String id) throws URISyntaxException {
        Resource<User> user = userData.load(id);
        String resId = userData.resourceId(user);
        Link selfRel = linkTo(UserController.class).slash(resId).withSelfRel();
        Resource<User> result = new Resource<User>(user.getContent(), selfRel);
        return ResponseEntity.ok(result);
    }
}
