package com.github.corneil.data_rest_demo.data_service.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.corneil.data_rest_demo.data_service.data.GroupMember;

@RepositoryRestResource(collectionResourceRel = "members", itemResourceRel = "member", path = "members")
public interface GroupMemberRepository extends CrudRepository<GroupMember, Long> {
    List<GroupMember> findByGroup_GroupName(@Param("groupName") String groupName);
    List<GroupMember> findByGroup_GroupNameAndEnabledTrue(@Param("groupName") String groupName);
    List<GroupMember> findByUser_UserIdAndEnabledTrue(@Param("userId") String userId);
    List<GroupMember> findByUser_UserId(@Param("userId") String userId);
}
