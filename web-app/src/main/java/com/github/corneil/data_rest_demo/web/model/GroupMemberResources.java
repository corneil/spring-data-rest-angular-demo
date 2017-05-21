package com.github.corneil.data_rest_demo.web.model;

import com.github.corneil.data_rest_demo.web.data.GroupMember;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * @author Corneil du Plessis
 */
public class GroupMemberResources extends Resources<Resource<GroupMember>> {
	public GroupMemberResources() {
	}

	public GroupMemberResources(Iterable<Resource<GroupMember>> content, Link... links) {
		super(content, links);
	}

	public GroupMemberResources(Iterable<Resource<GroupMember>> content, Iterable<Link> links) {
		super(content, links);
	}
}
