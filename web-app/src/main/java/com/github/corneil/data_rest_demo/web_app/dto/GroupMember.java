package com.github.corneil.data_rest_demo.web_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMember {
    @NotNull
    private Boolean enabled;
    @NotNull
    private String member;
    @NotNull
    private String memberOfgroup;
}
