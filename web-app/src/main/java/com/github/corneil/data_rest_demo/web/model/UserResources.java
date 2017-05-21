package com.github.corneil.data_rest_demo.web.model;

import com.github.corneil.data_rest_demo.web.data.User;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * @author Corneil du Plessis
 */
public class UserResources extends Resources<Resource<User>> {
	public UserResources() {
	}

	public UserResources(Iterable<Resource<User>> content, Iterable<Link> links) {
		super(content, links);
	}

	public UserResources(Iterable<Resource<User>> content, Link... links) {
		super(content, links);
	}
}
