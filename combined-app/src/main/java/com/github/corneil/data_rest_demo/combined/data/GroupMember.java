package com.github.corneil.data_rest_demo.combined.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "group_members", schema = "sd")
@Data
@EqualsAndHashCode(of = {"user", "group"})
public class GroupMember {
    @NotNull
    private Boolean enabled;
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "user_info")
    private User user;
    @NotNull
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "group_info")
    private Group group;
}
