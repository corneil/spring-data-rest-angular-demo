package com.github.corneil.data_rest_demo.web.model;

import com.github.corneil.data_rest_demo.web.data.Group;
import com.github.corneil.data_rest_demo.web.repository.GroupMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
 * @author Corneil du Plessis
 */
@Component
public class GroupEventListener extends AbstractRepositoryEventListener<Group> {
    private static Logger logger = LoggerFactory.getLogger(GroupEventListener.class);
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    public GroupEventListener(GroupMemberRepository groupMemberRepository) {
        this.groupMemberRepository = groupMemberRepository;
    }
    @Override
    protected void onBeforeDelete(Group entity) {
        logger.debug("delete:{}", entity.getId());
        groupMemberRepository.deleteByGroup_Id(entity.getId());
        super.onBeforeDelete(entity);
    }
}
