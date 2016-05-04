package com.github.corneil.data_rest_demo.combined.repository;

import com.github.corneil.data_rest_demo.combined.data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users", itemResourceRel = "user")
public interface UserRepository extends CrudRepository<User, Long> {
    User findOneByUserId(@Param("userId") String userId);
    @Query("select u from User u where UPPER(u.userId) like UPPER(:#{'%' + #input.toUpperCase() + '%'}) or UPPER(u.fullName) like UPPER(:#{'%' + #input.toUpperCase() + '%'})")
    @RestResource(rel = "find", path = "find", description = @Description("find users by fullname or userid containing input case-insensitive"))
    List<User> findLikeUserIdOrFullName(@Param("input") String input);
}
