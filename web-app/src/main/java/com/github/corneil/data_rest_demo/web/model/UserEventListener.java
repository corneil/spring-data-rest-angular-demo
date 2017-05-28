package com.github.corneil.data_rest_demo.web.model;

import com.github.corneil.data_rest_demo.web.data.User;
import com.github.corneil.data_rest_demo.web.repository.GroupMemberRepository;
import com.github.corneil.data_rest_demo.web.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
 * @author Corneil du Plessis
 */
@Component
@Slf4j
public class UserEventListener extends AbstractRepositoryEventListener<User> {
	private GroupRepository groupRepository;

	private GroupMemberRepository groupMemberRepository;

	@Autowired
	public UserEventListener(GroupMemberRepository groupMemberRepository, GroupRepository groupRepository) {
		this.groupMemberRepository = groupMemberRepository;
		this.groupRepository = groupRepository;
	}

	@Override
	protected void onBeforeDelete(User entity) {
		log.debug("delete:{}", entity.getUserId());
		long countOwnerships = groupRepository.countByGroupOwner_Id(entity.getId());
		if (countOwnerships > 0) {
			throw new UserOwnsGroupsException(String.format("%s owns %d groups", entity.getUserId(), countOwnerships));
		}
		groupMemberRepository.deleteByUser_Id(entity.getId());
		super.onBeforeDelete(entity);
	}
}
