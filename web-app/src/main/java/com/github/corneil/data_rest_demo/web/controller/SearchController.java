package com.github.corneil.data_rest_demo.web.controller;

import com.github.corneil.data_rest_demo.web.data.GroupMember;
import com.github.corneil.data_rest_demo.web.data.User;
import com.github.corneil.data_rest_demo.web.repository.GroupMemberRepository;
import com.github.corneil.data_rest_demo.web.repository.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by Corneil on 2017/05/13.
 */
@RepositoryRestController
@RequestMapping(value = "/rest/search")
public class SearchController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private GroupMemberRepository memberRepository;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<ResourceSupport> get() throws MalformedURLException {
		ResourceSupport result = new ResourceSupport();
		result.add(linkTo(methodOn(SearchController.class).findUsers(null, null)).withRel("users"));
		result.add(linkTo(methodOn(SearchController.class).findMembers(null, null, null)).withRel("members"));
		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<Resources<ResourceSupport>> findUsers(@RequestParam("input") String input, PersistentEntityResourceAssembler assembler) {
		List<ResourceSupport> result = new ArrayList<>();
		for (User user : userRepository.findLikeUserIdOrFullName(input)) {
			result.add(assembler.toResource(user));
		}
		Link self = linkTo(methodOn(SearchController.class).findUsers(null, null)).withSelfRel();
		return ResponseEntity.ok(new Resources<>(result, self));
	}

	@RequestMapping(value = "/members", method = RequestMethod.GET)
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
		Link self = linkTo(methodOn(SearchController.class).findMembers(null, null, null)).withSelfRel();
		return ResponseEntity.ok(new Resources<>(result, self));
	}
}
