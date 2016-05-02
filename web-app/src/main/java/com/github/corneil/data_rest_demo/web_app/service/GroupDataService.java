package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.Group;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.client.Hop.*;

/**
 * Created by Corneil on 2016-05-01.
 */
@Service("groupDataService")
public class GroupDataService extends AbstractDataService implements GroupDataInterface  {
    private static ParameterizedTypeReference<Resources<Resource<Group>>>
            groupsTypeRef =
            new ParameterizedTypeReference<Resources<Resource<Group>>>() {
            };
    private static ParameterizedTypeReference<Resource<Group>> groupTypeRef = new ParameterizedTypeReference<Resource<Group>>() {
    };
    private static final Logger logger = LoggerFactory.getLogger(GroupDataService.class);
    @Override
    public int countByGroupOwner(String userId) {
        logger.debug("countByGroupOwner:{}", userId);
        String response =
                getTraverson().follow("groups").follow("search")
                              .follow(rel("countByGroupOwner_UserId").withParameter("userId", userId))
                              .toObject(String.class);
        return Integer.parseInt(response);
    }
    @Override
    public Resource<Group> create(Group group) {
        String url = getTraverson().follow("groups").asLink().getHref();
        HttpEntity<Group> request = new HttpEntity<Group>(group);
        ResponseEntity<Resource<Group>> response = dataServiceClient.exchange(url, HttpMethod.POST, request, groupTypeRef);
        return response.getBody();
    }
    @Override
    public void delete(String id) {
        String url = getTraverson().follow("users").asLink().getHref();
        dataServiceClient.delete(url + "/" + id);
    }
    @Override
    public Resource<Group> find(String groupName) {
        return getTraverson().follow("groups").follow(rel("search")).follow(rel("findOneByGroupName").withParameter("groupName", groupName)).toObject(groupTypeRef);
    }
    @Override
    public Resources<Resource<Group>> findAll() {
        return getTraverson().follow("groups").toObject(groupsTypeRef);
    }
    @Override
    public Resource<Group> load(String id) {
        String url = getTraverson().follow("groups").asLink().getHref();
        ResponseEntity<Resource<Group>> response = dataServiceClient.exchange(url + "/" + id, HttpMethod.GET, null, groupTypeRef);
        return response.getBody();
    }
    @Override
    public String resourceId(Link selfRel) {
        return RestHelper.resourceId(selfRel.getHref(), getTraverson().follow("groups").asLink().getHref());
    }
    @Override
    public String resourceId(Resource<Group> self) {
        return resourceId(self.getLink("self"));
    }
    @Override
    public Resource<Group> save(String id, Resource<Group> group) {
        String url = getTraverson().follow("groups").asLink().getHref();
        HttpEntity<Group> request = new HttpEntity<Group>(group.getContent());
        ResponseEntity<Resource<Group>> response = dataServiceClient.exchange(url + "/" + id, HttpMethod.PUT, request, groupTypeRef);
        return response.getBody();
    }
}
