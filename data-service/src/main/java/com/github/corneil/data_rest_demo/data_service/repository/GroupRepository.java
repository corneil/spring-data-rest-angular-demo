package com.github.corneil.data_rest_demo.data_service.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.corneil.data_rest_demo.data_service.data.GroupInfo;

@RepositoryRestResource(collectionResourceRel = "groups", itemResourceRel = "group", path = "groups")
public interface GroupRepository extends CrudRepository<GroupInfo, Long> {
    GroupInfo findOneByGroupName(@Param("groupName") String groupName);
    Long countByGroupOwner_UserId(@Param("userId") String userId);
}
