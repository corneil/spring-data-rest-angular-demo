package com.github.corneil.data_rest_demo.data_service.model;

import com.github.corneil.data_rest_demo.data_service.data.GroupMember;
import com.github.corneil.data_rest_demo.data_service.data.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * Created by Corneil on 2016-07-21.
 */
@Projection(name = "detail", types = GroupMember.class)
public interface GroupMemberProjection {
    @Value("#{target.group.groupName}")
    String getGroupName();
    Boolean getEnabled();
    User getUser();
}
