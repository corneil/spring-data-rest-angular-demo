package com.github.corneil.data_rest_demo.web.controller;

import com.github.corneil.data_rest_demo.web.data.GroupMember;
import com.github.corneil.data_rest_demo.web.data.User;
import com.github.corneil.data_rest_demo.web.exceptions.EntityNotFound;
import com.github.corneil.data_rest_demo.web.repository.GroupMemberRepository;
import com.github.corneil.data_rest_demo.web.repository.UserRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Corneil du Plessis
 */
@RestController
@RequestMapping(path = "/simple")
@ApiModel(description = "Search")
public class SimpleController {
	private UserRepository userRepository;

	private GroupMemberRepository memberRepository;

	@Autowired
	public SimpleController(UserRepository userRepository, GroupMemberRepository memberRepository) {
		this.userRepository = userRepository;
		this.memberRepository = memberRepository;
	}

	@RequestMapping(path = "/users", method = RequestMethod.POST)
	@ApiOperation(value = "Create User")
	@Transactional
	public ResponseEntity<User> createUser(@RequestBody User user) {
		Assert.isNull(user.getId(), "Id cannot be provided when creating a User");
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}

	@RequestMapping(path = "/users/{id}", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User")
	@Transactional
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) throws EntityNotFound {
		User original = userRepository.findOne(id);
		if (original == null) {
			throw new EntityNotFound();
		}
		original.setDateOfBirth(user.getDateOfBirth());
		original.setEmailAddress(user.getEmailAddress());
		original.setFullName(user.getFullName());
		userRepository.save(original);
		return ResponseEntity.ok(original);
	}

	@RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get User")
	@ApiResponses({
	  	@ApiResponse(code = 200, message = "Success", response = User.class),
		@ApiResponse(code = 404, message = "Entity Not Found")
	})
	public ResponseEntity<User> getUser(@PathVariable("id") Long id) throws EntityNotFound {
		User user = userRepository.findOne(id);
		if (user == null) {
			throw new EntityNotFound();
		}
		return ResponseEntity.ok(user);
	}

	@RequestMapping(path = "/users/{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete User")
	@Transactional
	public ResponseEntity deleteUser(@PathVariable("id") long id) throws EntityNotFound {
		memberRepository.deleteByUser_Id(id);
		userRepository.delete(id);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(path = "/users/search", method = RequestMethod.GET)
	@ApiOperation(value = "Find Users")
	public ResponseEntity<List<User>> findUsers(@RequestParam("input") String input) {
		List<User> result = new ArrayList<>();
		for (User user : userRepository.findLikeUserIdOrFullName(input)) {
			result.add(user);
		}
		return ResponseEntity.ok(result);
	}

	@RequestMapping(path = "/members/search", method = RequestMethod.GET)
	@ApiOperation(value = "Find Members")
	public ResponseEntity<List<GroupMember>> findMembers(@RequestParam("groupName") final String groupName,
														 @RequestParam(name = "enabled", required = false) Boolean enabled) {
		List<GroupMember> members = enabled == null ?
			memberRepository.findByGroup_GroupName(groupName) :
			memberRepository.findByGroup_GroupNameAndEnabled(groupName, enabled);
		return ResponseEntity.ok(members);
	}
}
