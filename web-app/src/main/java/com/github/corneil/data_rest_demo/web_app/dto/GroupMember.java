package com.github.corneil.data_rest_demo.web_app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMember {
    @NotNull
    private Boolean enabled;
    private Long id;
    @NotNull
    private String member;
    @NotNull
    private String memberOfgroup;
}
