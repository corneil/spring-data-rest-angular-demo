package com.github.corneil.data_rest_demo.initial.repository;

import com.github.corneil.data_rest_demo.initial.data.GroupInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "groups", itemResourceRel = "group", path = "groups")
public interface GroupRepository extends CrudRepository<GroupInfo, Long> {
    GroupInfo findOneByGroupName(@Param("groupName") String groupName);
    Long countByGroupOwner_UserId(@Param("userId") String userId);
}
