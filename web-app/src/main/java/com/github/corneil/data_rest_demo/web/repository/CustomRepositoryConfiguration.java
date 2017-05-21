package com.github.corneil.data_rest_demo.web.repository;

import com.github.corneil.data_rest_demo.web.data.Group;
import com.github.corneil.data_rest_demo.web.data.GroupMember;
import com.github.corneil.data_rest_demo.web.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

/**
 * @author Corneil du Plessis
 */
@Configuration
public class CustomRepositoryConfiguration {
	private static class GroupMemberResourceProcessor implements ResourceProcessor<Resource<GroupMember>> {
		public GroupMemberResourceProcessor(EntityLinks entityLinks) {
			this.entityLinks = entityLinks;
		}

		protected EntityLinks entityLinks;

		@Override
		public Resource<GroupMember> process(Resource<GroupMember> resource) {
			resource.add(entityLinks.linkForSingleResource(Group.class, resource.getContent().getGroup().getId()).withRel("_group"));
			resource.add(entityLinks.linkForSingleResource(User.class, resource.getContent().getUser().getId()).withRel("_user"));
			return resource;
		}
	}

	private static class UserResourceProcessor implements ResourceProcessor<Resource<User>> {
		ResourceLoader resourceLoader;

		public UserResourceProcessor(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}

		@Override
		public Resource<User> process(Resource<User> userResource) {
			String imageUrl = String.format("/assets/users/%s.png", userResource.getContent().getUserId());
			userResource.getContent().setHasImage(resourceLoader.getResource("classpath:/static" + imageUrl).exists());
			return userResource;
		}
	}

	private static class GroupResourceProcessor implements ResourceProcessor<Resource<Group>> {
		public GroupResourceProcessor(EntityLinks entityLinks) {
			this.entityLinks = entityLinks;
		}

		protected EntityLinks entityLinks;

		@Override
		public Resource<Group> process(Resource<Group> resource) {
			resource.add(entityLinks.linkForSingleResource(User.class, resource.getContent().getGroupOwner().getId()).withRel("_groupOwner"));
			return resource;
		}
	}

	@Bean
	@Autowired
	public ResourceProcessor<Resource<User>> userProcessor(ResourceLoader resourceLoader) {
		return new UserResourceProcessor(resourceLoader);
	}

	@Bean
	@Autowired
	public ResourceProcessor<Resource<GroupMember>> memberProcessor(EntityLinks entityLinks) {
		return new GroupMemberResourceProcessor(entityLinks);
	}

	@Bean
	@Autowired
	public ResourceProcessor<Resource<Group>> groupProcessor(EntityLinks entityLinks) {
		return new GroupResourceProcessor(entityLinks);
	}
}
