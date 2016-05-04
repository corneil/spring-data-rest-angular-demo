package com.github.corneil.data_rest_demo.combined.repository;

import com.github.corneil.data_rest_demo.combined.data.Group;
import com.github.corneil.data_rest_demo.combined.data.GroupMember;
import com.github.corneil.data_rest_demo.combined.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

/**
 * Created by Corneil on 2016/04/18.
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

    private static class GroupResourceProcessor implements ResourceProcessor<Resource<Group>> {
        public GroupResourceProcessor(EntityLinks entityLinks) {
            this.entityLinks = entityLinks;
        }
        protected EntityLinks entityLinks;
        @Override
        public Resource<Group> process(Resource<Group> resource) {
            resource.add(entityLinks.linkForSingleResource(User.class, resource.getContent().getGroupOwner().getId())
                                    .withRel("_groupOwner"));
            return resource;
        }
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
