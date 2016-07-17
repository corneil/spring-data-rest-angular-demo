package com.github.corneil.data_rest_demo.data_service.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
    @JoinColumn(name = "user_a")
    private User user;
    @NotNull
    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "group_a")
    private Group group;
}
