package com.github.corneil.data_rest_demo.initial.repository;

import com.github.corneil.data_rest_demo.initial.data.GroupInfo;
import com.github.corneil.data_rest_demo.initial.data.GroupMember;
import com.github.corneil.data_rest_demo.initial.data.UserInfo;
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
            resource.add(entityLinks.linkForSingleResource(GroupInfo.class, resource.getContent().getMemberOfgroup().getId())
                                    .withRel("_memberOfgroup"));
            resource.add(entityLinks.linkForSingleResource(UserInfo.class, resource.getContent().getMember().getId()).withRel("_member"));
            return resource;
        }
    }

    private static class GroupResourceProcessor implements ResourceProcessor<Resource<GroupInfo>> {
        public GroupResourceProcessor(EntityLinks entityLinks) {
            this.entityLinks = entityLinks;
        }
        protected EntityLinks entityLinks;
        @Override
        public Resource<GroupInfo> process(Resource<GroupInfo> resource) {
            resource.add(entityLinks.linkForSingleResource(UserInfo.class, resource.getContent().getGroupOwner().getId())
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
    public ResourceProcessor<Resource<GroupInfo>> groupProcessor(EntityLinks entityLinks) {
        return new GroupResourceProcessor(entityLinks);
    }
}
