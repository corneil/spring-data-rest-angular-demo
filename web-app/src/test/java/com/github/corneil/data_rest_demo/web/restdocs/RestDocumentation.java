package com.github.corneil.data_rest_demo.web.restdocs;

import lombok.extern.slf4j.XSlf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Corneil on 2017/05/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@XSlf4j
public class RestDocumentation {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private WebApplicationContext context;
    private RestDocumentationResultHandler documentationHandler;
    private MockMvc mockMvc;

    public static LinksSnippet links(LinkDescriptor... descriptors) {
        return HypermediaDocumentation.links(halLinks(),
                linkWithRel("self").optional().description("Link to resource."),
                linkWithRel("profile").optional().ignored()).and(descriptors);
    }

    public static LinksSnippet links() {
        return HypermediaDocumentation.links(halLinks(),
                linkWithRel("self").optional().description("Link to resource."),
                linkWithRel("profile").optional().ignored());
    }

    @Test
    public void groupFind() {
        try {
            this.mockMvc.perform(get("/rest/groups/search/findOneByGroupName").param("groupName", "rebels").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(links(linkWithRel("group").description("The <<resources-group-get, Group resource>>"),
                                linkWithRel("groupOwner").description("The <<resources-user-get, Owner of Group>>"),
                                linkWithRel("_groupOwner").description("Cacheable <<resources-user-get, Owner of Group>>"),
                                linkWithRel("members").description("The <<resources-member-get, Members of Group resource>>")),
                                requestParameters(parameterWithName("groupName").description("Name of group")),
                                responseFields(groupResource(""))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    @Test
    public void groupGet() {
        try {
            this.mockMvc.perform(get("/rest/groups/{id}", 1L).contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Id of group")),
                                links(linkWithRel("group").description("The <<resources-group-get, Group resource>>"),
                                        linkWithRel("groupOwner").description("The <<resources-user-get, Owner of Group>>"),
                                        linkWithRel("_groupOwner").description("Cacheable <<resources-user-get, Owner of Group>>"),
                                        linkWithRel("members").description("The <<resources-member-get, Members of Group resource>>")),
                                responseFields(groupResource(""))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    private List<FieldDescriptor> groupResource(String prefix) {
        return Arrays.asList(subsectionWithPath(prefix + "_links").description("<<resources-get-group_links,Links>> to group related resources"),
                fieldWithPath(prefix + "groupName").description("The group's unique name"),
                fieldWithPath(prefix + "description").description("The groups's description"));
    }

    @Test
    public void groupsList() {
        try {
            final List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
            fieldDescriptors.add(subsectionWithPath("_links").description("<<resources-groups-list_links,Links>> to resource"));
            fieldDescriptors.addAll(groupsResponse("_embedded."));
            this.mockMvc.perform(get("/rest/groups").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(links(linkWithRel("search").description(
                                "The <<resources-groups-search, Users resource search>>")), responseFields(fieldDescriptors)));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    private List<FieldDescriptor> groupsResponse(String prefix) {
        List<FieldDescriptor> result = new ArrayList<>();
        result.add(subsectionWithPath(prefix + "groups[]").description("The group resources"));
        result.addAll(groupResource(prefix + "groups[]."));
        return result;
    }

    @Test
    public void index() {
        try {
            this.mockMvc.perform(get("/rest").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(responseFields(subsectionWithPath("_links").description(
                                "<<resources-index-links,Links>> to other resources")),
                                links(linkWithRel("users").description("The <<resources-users,Users resource>>"),
                                        linkWithRel("groups").description("The <<resources-groups,Groups resource>>"),
                                        linkWithRel("members").description("The <<resources-members,Members resource>>"))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    @Test
    public void memberGet() {
        try {
            this.mockMvc.perform(get("/rest/members/{id}", 1L).contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Id of member")),
                                links(linkWithRel("members").description("The <<resources-member-get, Member resource>>"),
                                        linkWithRel("user").description("The <<resources-user-get, User resource>>"),
                                        linkWithRel("_user").description("Cacheable <<resources-user-get, User resource>>"),
                                        linkWithRel("group").description("The <<resources-group-get, Group resource>>"),
                                        linkWithRel("_group").description("Cacheable <<resources-group-get, Group resource>>")),
                                responseFields(memberResourceFields(""))));
        } catch (Throwable x) {
            log.error("memberGet:{}", x.toString(), x);
        }
    }

    private List<FieldDescriptor> memberResourceFields(final String prefix) {
        return Arrays.asList(subsectionWithPath(prefix + "_links").description("<<resources-member-get_links,Links>> to Member related resources"),
                fieldWithPath(prefix + "enabled").description("Indicator that group membership is enabled").type(JsonFieldType.BOOLEAN));
    }


    @Before
    public void setUp() {
        this.documentationHandler = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                                      .apply(documentationConfiguration(this.restDocumentation))
                                      .alwaysDo(this.documentationHandler)
                                      .build();
    }

    @Test
    public void userGet() {
        try {
            final List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
            fieldDescriptors.addAll(userResource(""));
            this.mockMvc.perform(get("/rest/users/{id}", 1L).contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Id of user")),
                                links(linkWithRel("user").description("The <<resources-user-get, Users resource>>")),
                                responseFields(fieldDescriptors)));
        } catch (Throwable x) {
            log.error("userGet:{}", x.toString(), x);
        }
    }

    private List<FieldDescriptor> userResource(String prefix) {
        return Arrays.asList(subsectionWithPath(prefix + "_links").description("<<resources-user-get_links,Links>> to other resources"),
                fieldWithPath(prefix + "userId").description("The user's userId"),
                fieldWithPath(prefix + "fullName").description("The user's full name and surname"),
                fieldWithPath(prefix + "dateOfBirth").description("The user's date of birth"),
                fieldWithPath(prefix + "emailAddress").description("The user's email address"),
                fieldWithPath(prefix + "hasImage").description("Indicator that user has an image").type(JsonFieldType.BOOLEAN));
    }

    @Test
    public void usersList() {
        try {
            final List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
            fieldDescriptors.add(subsectionWithPath("_links").description("<<resources-users-list_links,Links>> to related resources"));
            fieldDescriptors.addAll(usersResponse("_embedded."));
            this.mockMvc.perform(get("/rest/users").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(links(linkWithRel("search").description(
                                "The <<resources-users-search, Users resource search>>")), responseFields(fieldDescriptors)));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    private List<FieldDescriptor> usersResponse(String prefix) {
        List<FieldDescriptor> result = new ArrayList<>();
        result.add(subsectionWithPath(prefix + "users[]").description("The user resources"));
        result.addAll(userResource(prefix + "users[]."));
        return result;
    }
	@Test
	public void searchMembers() {
		try {
			List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
			fieldDescriptors.add(subsectionWithPath("_links").description("<<resources-search-members_links,Links>> to resources"));
			fieldDescriptors.addAll(memberResourceFields("_embedded.members[]."));
			this.mockMvc.perform(get("/rest/search/members")
									.param("groupName", "rebels")
									.param("enabled","true")
									.contentType("application/hal+json"))
						.andExpect(status().isOk())
						.andDo(this.documentationHandler.document(links(),
							requestParameters(
								parameterWithName("groupName").description("groupName of groups to find"),
								parameterWithName("enabled").description("Optional indicator to find enabled or disabled groups").optional()),
							responseFields(fieldDescriptors)));
		} catch (Throwable x) {
			log.error("membersGroupNameEnabled:{}", x.toString(), x);
		}
	}

    @Test
    public void searchUsers() {
        try {
            final List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
            fieldDescriptors.add(subsectionWithPath("_links").description("<<resources-search-users_links,Links>> to related resources"));
            fieldDescriptors.addAll(usersResponse("_embedded."));
            this.mockMvc.perform(get("/rest/search/users").param("input", "luke").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(requestParameters(parameterWithName("input").description("Substring to match")),
                                links(),
                                responseFields(fieldDescriptors)));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }
}
