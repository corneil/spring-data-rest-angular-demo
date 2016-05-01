package com.github.corneil.data_rest_demo.initial.repository;

import com.github.corneil.data_rest_demo.initial.data.GroupMember;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "groupMembers", itemResourceRel = "groupMember", path = "group-member")
public interface GroupMemberRepository extends CrudRepository<GroupMember, Long> {
    List<GroupMember> findByMemberOfgroup_GroupName(@Param("groupName") String groupName);
    List<GroupMember> findByMemberOfgroup_GroupNameAndEnabledTrue(@Param("groupName") String groupName);
    List<GroupMember> findByMember_UserIdAndEnabledTrue(@Param("userId") String userId);
    List<GroupMember> findByMember_UserId(@Param("userId") String userId);
}
