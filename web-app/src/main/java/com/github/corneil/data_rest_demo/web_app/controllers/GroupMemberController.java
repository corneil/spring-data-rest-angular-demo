package com.github.corneil.data_rest_demo.web_app.controllers;

import com.github.corneil.data_rest_demo.web_app.dto.GroupMember;
import com.github.corneil.data_rest_demo.web_app.service.GroupDataInterface;
import com.github.corneil.data_rest_demo.web_app.service.GroupMemberInterface;
import com.github.corneil.data_rest_demo.web_app.service.UserDataInterface;
import com.github.corneil.data_rest_demo.web_app.util.RestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping(path = "/rest/members")
public class GroupMemberController extends AbstractRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GroupMemberController.class);
    public GroupMemberController() {
        super(logger);
    }
    @Autowired
    protected GroupDataInterface groupData;
    @Autowired
    protected GroupMemberInterface memberData;
    @Autowired
    protected UserDataInterface userData;
    private Resource<GroupMember> createResponse(Resource<GroupMember> item) {
        logger.debug("createResponse:{}", item);
        String itemId = memberData.resourceId(item);
        Link selfRel = linkTo(GroupMemberController.class).slash(itemId).withSelfRel();
        Resource<GroupMember> result = new Resource<GroupMember>(item.getContent(), selfRel);
        String groupMemberId = userData.resourceId(item.getLink("_member"));
        String memberOfGroupId = groupData.resourceId(item.getLink("_memberOfgroup"));
        Link groupMember = linkTo(UserController.class).slash(groupMemberId).withRel("groupMember");
        result.add(groupMember);
        Link memberOfgroup = linkTo(GroupController.class).slash(memberOfGroupId).withRel("memberOfgroup");
        result.add(memberOfgroup);
        result.getContent().setMember(groupMember.getHref());
        result.getContent().setMemberOfgroup(memberOfgroup.getHref());
        return result;
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<GroupMember>>> findAll() {
        logger.debug("findAll");
        Resources<Resource<GroupMember>> response = memberData.findAll();
        List<Resource<GroupMember>> content = new ArrayList<Resource<GroupMember>>();
        for (Resource<GroupMember> member : response.getContent()) {
            Resource<GroupMember> item = createResponse(member);
            content.add(item);
        }
        Link membersSelfRel = linkTo(GroupMemberController.class).withSelfRel();
        Resources<Resource<GroupMember>> result = new Resources<Resource<GroupMember>>(content, membersSelfRel);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource<GroupMember>> create(@RequestBody GroupMember groupMember) {
        logger.debug("create:{}", groupMember);
        prepareForUpdate(groupMember);
        return ResponseEntity.ok(createResponse(memberData.create(groupMember)));
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Resource<GroupMember>> save(String id, @RequestBody GroupMember groupMember) {
        logger.debug("save:{},{}", id, groupMember);
        prepareForUpdate(groupMember);
        Link selfLink = new Link(groupData.resourceLink(id), "self");
        return ResponseEntity.ok(createResponse(memberData.save(id, new Resource<GroupMember>(groupMember, selfLink))));
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Resource<GroupMember>> patch(@PathVariable String id, @RequestBody GroupMember groupMember) {
        Assert.notNull(id, "id is required");
        logger.debug("path:{},{}", id, groupMember);
        return ResponseEntity.ok(createResponse(memberData.patch(id, groupMember)));
    }
    private void prepareForUpdate(@RequestBody GroupMember groupMember) {
        String memberId = RestHelper.resourceId(groupMember.getMember(), linkTo(UserController.class).toUri().toString());
        String groupId = RestHelper.resourceId(groupMember.getMemberOfgroup(), linkTo(GroupController.class).toUri().toString());
        groupMember.setMember(userData.resourceLink(memberId));
        groupMember.setMemberOfgroup(groupData.resourceLink(groupId));
    }
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public ResponseEntity<Resources<Resource<GroupMember>>> findByGroupName(@RequestParam("groupName") String groupName) {
        logger.debug("findByGroupName:{}", groupName);
        Resources<Resource<GroupMember>> response = memberData.findByMemberOfGroup(groupName);
        List<Resource<GroupMember>> content = new ArrayList<Resource<GroupMember>>();
        for (Resource<GroupMember> member : response.getContent()) {
            Resource<GroupMember> item = createResponse(member);
            content.add(item);
        }
        Link membersSelfRel = linkTo(GroupMemberController.class).withSelfRel();
        Resources<Resource<GroupMember>> result = new Resources<Resource<GroupMember>>(content, membersSelfRel);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Resource<GroupMember>> load(@PathVariable String id) {
        logger.debug("load:{}", id);
        Resource<GroupMember> response = memberData.load(id);
        Resource<GroupMember> result = createResponse(response);
        return ResponseEntity.ok(result);
    }
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable String id) {
        logger.debug("delete:{}", id);
        memberData.delete(id);
        return ResponseEntity.ok().build();
    }
}
