package com.github.corneil.data_rest_demo.web.restdocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.XSlf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Corneil du Plessis
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@XSlf4j
public class ApiDocumentation {
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	@Autowired
	private WebApplicationContext context;

	private RestDocumentationResultHandler documentationHandler;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void createUser() throws Exception {
		Map<String, Object> user = new HashMap<>();
		user.put("userId", "john-doe");
		user.put("dateOfBirth", "1971-11-12");
		user.put("emailAddress", "john.doe@email.com");
		user.put("fullName", "John Doe");
		this.mockMvc.perform(post("/simple/users").contentType("application/json").content(objectMapper.writeValueAsString(user)))
					.andExpect(status().isOk())
					.andDo(this.documentationHandler.document(responseFields(userResource(""))));
	}

	@Test
	public void deleteUser() throws Exception {
		this.mockMvc.perform(delete("/simple/users/{id}", 2))
					.andExpect(status().isOk())
					.andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Id of user"))));
	}

	@Test
	public void getUser() throws Exception {
		this.mockMvc.perform(get("/simple/users/{id}", 1L).contentType("application/json"))
					.andExpect(status().isOk())
					.andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Id of user")),
						responseFields(userResource(""))));
	}

	private List<FieldDescriptor> memberResourceFields(final String prefix) {
		List<FieldDescriptor> result = new ArrayList<>();
		result.add(subsectionWithPath(prefix + "user").description("<<user-resource,User>>"));
		result.add(subsectionWithPath(prefix + "group").description("<<group-resource,Group>>"));
		result.add(fieldWithPath(prefix + "id").description("Unique id of GroupMember").type(JsonFieldType.NUMBER));
		result.add(fieldWithPath(prefix + "enabled").description("Indicator that group membership is enabled").type(JsonFieldType.BOOLEAN));
		result.addAll(userResource(prefix + "user."));
		return result;
	}

	@Test
	public void searchMember() throws Exception {
		this.mockMvc.perform(get("/simple/members/search").param("groupName", "rebels").param("enabled", "true").contentType("application/json"))
					.andExpect(status().isOk())
					.andDo(this.documentationHandler.document(requestParameters(parameterWithName("groupName").description("groupName of groups to find"),
						parameterWithName("enabled").description("Optional indicator to find enabled or disabled groups").optional()),
						responseFields(memberResourceFields("[]."))));
	}

	@Test
	public void searchUser() throws Exception {
		this.mockMvc.perform(get("/simple/users/search").param("input", "luke").contentType("application/json"))
					.andExpect(status().isOk())
					.andDo(this.documentationHandler.document(requestParameters(parameterWithName("input").description("Substring to match")),
						responseFields(userResource("[]."))));
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
	public void updateUser() throws Exception {
		Map<String, Object> user = new HashMap<>();
		user.put("dateOfBirth", "1971-11-12");
		user.put("emailAddress", "john.doe@email.com");
		user.put("fullName", "John Doe");
		this.mockMvc.perform(put("/simple/users/{id}", 1).contentType("application/json").content(objectMapper.writeValueAsString(user)))
					.andExpect(status().isOk())
					.andDo(this.documentationHandler.document(pathParameters(parameterWithName("id").description("Id of user")),
						responseFields(userResource(""))));
	}

	private List<FieldDescriptor> userResource(String prefix) {
		return Arrays.asList(fieldWithPath(prefix + "id").description("Unique id of User record").optional(),
			fieldWithPath(prefix + "userId").description("The user's userId"),
			fieldWithPath(prefix + "fullName").description("The user's full name and surname"),
			fieldWithPath(prefix + "dateOfBirth").description("The user's date of birth"),
			fieldWithPath(prefix + "emailAddress").description("The user's email address"),
			fieldWithPath(prefix + "hasImage").description("Indicator that user has an image").optional().type(JsonFieldType.BOOLEAN));
	}
}
