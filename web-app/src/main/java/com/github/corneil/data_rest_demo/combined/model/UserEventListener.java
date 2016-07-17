package com.github.corneil.data_rest_demo.combined.model;

import com.github.corneil.data_rest_demo.combined.data.User;
import com.github.corneil.data_rest_demo.combined.repository.GroupMemberRepository;
import com.github.corneil.data_rest_demo.combined.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
 * Created by Corneil on 2016-05-04.
 */
@Component
public class UserEventListener extends AbstractRepositoryEventListener<User> {
    private static Logger logger = LoggerFactory.getLogger(UserEventListener.class);
    private GroupRepository groupRepository;
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    public UserEventListener(GroupMemberRepository groupMemberRepository, GroupRepository groupRepository) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepository = groupRepository;
    }
    @Override
    protected void onBeforeDelete(User entity) {
        logger.debug("delete:{}", entity.getUserId());
        long countOwnerships = groupRepository.countByGroupOwner_Id(entity.getId());
        if (countOwnerships > 0) {
            throw new UserOwnsGroupsException(String.format("%s owns %d groups", entity.getUserId(), countOwnerships));
        }
        groupMemberRepository.deleteByUser_Id(entity.getId());
        super.onBeforeDelete(entity);
    }
}
