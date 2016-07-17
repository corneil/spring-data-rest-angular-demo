package com.github.corneil.data_rest_demo.combined.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.github.corneil.data_rest_demo.combined.data.Group;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "groups", itemResourceRel = "group", path = "groups")
public interface GroupRepository extends CrudRepository<Group, Long> {
    Group findOneByGroupName(@Param("groupName") String groupName);
    @RestResource(exported = false)
    Long countByGroupOwner_Id(@Param("id") Long id);
}
