package com.github.corneil.data_rest_demo.combined.repository;

import com.github.corneil.data_rest_demo.combined.data.Group;
import com.github.corneil.data_rest_demo.combined.data.GroupMember;
import com.github.corneil.data_rest_demo.combined.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "members", itemResourceRel = "members", path = "members")
public interface GroupMemberRepository extends CrudRepository<GroupMember, Long> {
    List<GroupMember> findByGroup_GroupName(@Param("groupName") String groupName);
    List<GroupMember> findByGroup_GroupNameAndEnabledTrue(@Param("groupName") String groupName);
    List<GroupMember> findByUser_UserIdAndEnabledTrue(@Param("userId") String userId);
    List<GroupMember> findByUser_UserId(@Param("userId") String userId);
    List<GroupMember> findByUser(@Param("member") User member);
    List<GroupMember> findByUserAndEnabledTrue(@Param("member") User member);
    List<GroupMember> findByGroup(@Param("group") Group group);
    List<GroupMember> findByGroupAndEnabledTrue(@Param("group") Group group);
    @RestResource(exported = false)
    void deleteByGroup_Id(@Param("id") Long id);
    @RestResource(exported = false)
    void deleteByUser_Id(@Param("id") Long id);
}
