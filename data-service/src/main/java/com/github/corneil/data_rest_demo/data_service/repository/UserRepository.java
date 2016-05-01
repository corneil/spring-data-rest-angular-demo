package com.github.corneil.data_rest_demo.initial.repository;

import com.github.corneil.data_rest_demo.initial.data.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users", itemResourceRel = "user")
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findOneByUserId(@Param("userId") String userId);
    @Query("select u from UserInfo u where UPPER(u.userId) like UPPER(:#{'%' + #input.toUpperCase() + '%'}) or UPPER(u.fullName) like UPPER(:#{'%' + #input.toUpperCase() + '%'})")
    List<UserInfo> findLikeUserIdOrFullName(@Param("input") String input);
}
