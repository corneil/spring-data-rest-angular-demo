package com.github.corneil.data_rest_demo.web_app.service;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import com.github.corneil.data_rest_demo.web_app.dto.GroupMember;

public interface GroupMemberInterface {
    Resource<GroupMember> create(GroupMember member);
    void delete(String id);
    Resources<Resource<GroupMember>> findAll();
    Resources<Resource<GroupMember>> findByMemberOfGroup(String groupName);
    Resource<GroupMember> load(String id);
    String resourceId(Resource<GroupMember> member);
    String resourceLink(String id);
    Resource<GroupMember> save(String id, Resource<GroupMember> member);

}
