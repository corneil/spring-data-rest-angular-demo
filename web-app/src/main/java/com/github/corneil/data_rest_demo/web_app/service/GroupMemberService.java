package com.github.corneil.data_rest_demo.web_app.service;

import com.github.corneil.data_rest_demo.web_app.dto.GroupMember;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static org.springframework.hateoas.client.Hop.*;

@Service("groupMemberService")
public class GroupMemberService extends AbstractDataService implements GroupMemberInterface {
    private static ParameterizedTypeReference<Resources<Resource<GroupMember>>>
            membersTypeRef =
            new ParameterizedTypeReference<Resources<Resource<GroupMember>>>() {
            };
    private static ParameterizedTypeReference<Resource<GroupMember>>
            memberTypeRef =
            new ParameterizedTypeReference<Resource<GroupMember>>() {
            };
    @Override
    public Resource<GroupMember> create(GroupMember member) {
        String url = getTraverson().follow(rel("groupMembers")).asLink().getHref();
        HttpEntity<GroupMember> request = new HttpEntity<GroupMember>(member);
        ResponseEntity<Resource<GroupMember>> response = dataServiceClient.exchange(url, HttpMethod.POST, request, memberTypeRef);
        // TODO process status
        return response.getBody();
    }
    @Override
    public void delete(String id) {
        String url = getTraverson().follow(rel("groupMembers")).asLink().getHref();
        dataServiceClient.delete(url + "/" + id);
        // TODO process status
    }
    @Override
    public Resources<Resource<GroupMember>> findAll() {
        return getTraverson().follow(rel("groupMembers")).toObject(membersTypeRef);
    }
    @Override
    public Resources<Resource<GroupMember>> findByMemberOfGroup(String groupName) {
        return getTraverson().follow(rel("groupMembers"))
                             .follow(rel("search"))
                             .follow(rel("findByMemberOfgroup_GroupName").withParameter("groupName", groupName))
                             .toObject(membersTypeRef);
    }
    @Override
    public Resource<GroupMember> load(String id) {
        String url = getTraverson().follow(rel("groupMembers")).asLink().getHref();
        ResponseEntity<Resource<GroupMember>> response = dataServiceClient.exchange(url + "/" + id, HttpMethod.GET, null, memberTypeRef);
        return response.getBody();
    }
    @Override
    public String resourceId(Resource<GroupMember> member) {
        Assert.notNull(member);
        return RestHelper.resourceId(member.getLink("self").getHref(), getTraverson().follow(rel("groupMembers")).asLink().getHref());
    }
    @Override
    public String resourceLink(String id) {
        return getTraverson().follow(rel("groupMembers")).asLink() + "/" + id;
    }
    @Override
    public Resource<GroupMember> save(String id, Resource<GroupMember> member) {
        String url = getTraverson().follow(rel("groupMembers")).asLink().getHref();
        HttpEntity<GroupMember> request = new HttpEntity<GroupMember>(member.getContent());
        ResponseEntity<Resource<GroupMember>> response = dataServiceClient.exchange(url + "/" + id, HttpMethod.PUT, request, memberTypeRef);
        // TODO process status
        return response.getBody();
    }
}
