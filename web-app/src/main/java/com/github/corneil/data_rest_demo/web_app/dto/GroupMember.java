package com.github.corneil.data_rest_demo.web_app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GroupMember {
    @NotNull
    private Boolean enabled;
    private Long id;
    @NotNull
    private User member;
    @NotNull
    private Group memberOfgroup;
}
