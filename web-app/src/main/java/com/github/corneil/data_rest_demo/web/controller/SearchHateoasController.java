package com.github.corneil.data_rest_demo.web.controller;

import com.github.corneil.data_rest_demo.web.data.GroupMember;
import com.github.corneil.data_rest_demo.web.data.User;
import com.github.corneil.data_rest_demo.web.model.GroupMemberResources;
import com.github.corneil.data_rest_demo.web.model.UserResources;
import com.github.corneil.data_rest_demo.web.repository.GroupMemberRepository;
import com.github.corneil.data_rest_demo.web.repository.UserRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * @author Corneil du Plessis
 */
@RepositoryRestController
@RequestMapping(path = "/rest/search")
public class SearchHateoasController {
	private UserRepository userRepository;

	private GroupMemberRepository memberRepository;

	@Autowired
	public SearchHateoasController(UserRepository userRepository, GroupMemberRepository memberRepository) {
		this.userRepository = userRepository;
		this.memberRepository = memberRepository;
	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	@ApiOperation(value = "API", hidden = true)
	public ResponseEntity<ResourceSupport> get() throws MalformedURLException {
		ResourceSupport result = new ResourceSupport();
		result.add(linkTo(methodOn(SearchHateoasController.class).findUsers(null, null)).withRel("users"));
		result.add(linkTo(methodOn(SearchHateoasController.class).findMembers(null, null, null)).withRel("members"));
		return ResponseEntity.ok(result);
	}

	@RequestMapping(path = "/users", method = RequestMethod.GET)
	@ApiOperation(value = "Find Users", response = UserResources.class)
	public ResponseEntity<Resources<ResourceSupport>> findUsers(@RequestParam("input") String input, PersistentEntityResourceAssembler assembler) {
		List<ResourceSupport> result = new ArrayList<>();
		for (User user : userRepository.findLikeUserIdOrFullName(input)) {
			result.add(assembler.toResource(user));
		}
		Link self = linkTo(methodOn(SearchHateoasController.class).findUsers(null, null)).withSelfRel();
		return ResponseEntity.ok(new Resources<>(result, self));
	}

	@RequestMapping(path = "/members", method = RequestMethod.GET)
	@ApiOperation(value = "Find Members", response = GroupMemberResources.class)
	public ResponseEntity<Resources<ResourceSupport>> findMembers(@RequestParam("groupName") final String groupName,
																  @RequestParam(name = "enabled", required = false) Boolean enabled,
																  PersistentEntityResourceAssembler assembler) {
		List<GroupMember> members = enabled == null ?
			memberRepository.findByGroup_GroupName(groupName) :
			memberRepository.findByGroup_GroupNameAndEnabled(groupName, enabled);
		List<ResourceSupport> result = new ArrayList<>();
		for (GroupMember member : members) {
			result.add(assembler.toResource(member));
		}
		Link self = linkTo(methodOn(SearchHateoasController.class).findMembers(null, null, null)).withSelfRel();
		return ResponseEntity.ok(new Resources<>(result, self));
	}
}
