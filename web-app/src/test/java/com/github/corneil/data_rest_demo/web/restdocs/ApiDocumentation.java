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
public class ApiDocumentation {
    private MockMvc mockMvc;
    private RestDocumentationResultHandler documentationHandler;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private WebApplicationContext context;

    public static LinksSnippet links(LinkDescriptor... descriptors) {
        return HypermediaDocumentation.links(halLinks(),linkWithRel("self").optional().description("Link to resource."), linkWithRel("profile").optional().ignored()).and(descriptors);
    }
    public static LinksSnippet links() {
        return HypermediaDocumentation.links(halLinks(),linkWithRel("self").optional().description("Link to resource."), linkWithRel("profile").optional().ignored());
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
    public void index() {
        try {
            this.mockMvc.perform(get("/rest").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(
                                responseFields(subsectionWithPath("_links").description(
                                "<<resources-index-links,Links>> to other resources")),
                                links(
                                        linkWithRel("users").description("The <<resources-users,Users resource>>"),
                                        linkWithRel("groups").description("The <<resources-groups,Groups resource>>"),
                                        linkWithRel("members").description("The <<resources-members,Members resource>>"))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    @Test
    public void usersList() {
        try {
            this.mockMvc.perform(get("/rest/users").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(links(
                                linkWithRel("search").description("The <<resources-users-search, Users resource search>>")),
                                responseFields(usersResponse("_embedded.users[]."))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    private List<FieldDescriptor> userResource(String prefix) {
        return Arrays.asList(subsectionWithPath("_links").description("<<resources-index-links,Links>> to other resources"),
                fieldWithPath(prefix + "userId").description("The user's userId"),
                fieldWithPath(prefix + "fullName").description("The user's full name and surname"),
                fieldWithPath(prefix + "dateOfBirth").description("The user's date of birth"),
                fieldWithPath(prefix + "emailAddress").description("The user's email address"),
                fieldWithPath(prefix + "hasImage").description("Indicator that user has an image").type(JsonFieldType.BOOLEAN));
    }

    private List<FieldDescriptor> usersResponse(String prefix) {
        List<FieldDescriptor> result = new ArrayList<>();
        result.add(subsectionWithPath("_embedded.users[]").description("The user resources"));
        result.addAll(userResource(prefix));
        return result;
    }

    @Test
    public void userGet() {
        try {
            this.mockMvc.perform(get("/rest/users/{id}", 1L).contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Substring to match")),
                                links(linkWithRel("user").description("The <<resources-users-get, Users resource>>")),
                                responseFields(userResource(""))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }

    @Test
    public void usersSearchPartial() {
        try {
            this.mockMvc.perform(get("/rest/users/search/find").param("input", "luke").contentType("application/hal+json"))
                        .andExpect(status().isOk())
                        .andDo(this.documentationHandler.document(requestParameters(parameterWithName("input").description("Substring to match")),
                                links(),
                                responseFields(usersResponse("_embedded.users[]."))));
        } catch (Throwable x) {
            log.error("userSearchPartial:{}", x.toString(), x);
        }
    }
}
