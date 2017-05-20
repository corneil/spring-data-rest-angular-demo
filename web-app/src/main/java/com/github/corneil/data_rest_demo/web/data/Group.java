package com.github.corneil.data_rest_demo.web.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "user_groups", schema = "sd")
@Data
@EqualsAndHashCode(of = {"groupName"})
@ToString(exclude = {"members"})
public class Group {
    @NotNull
    private String description;
    @NotNull
    @Column(unique = true)
    private String groupName;
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "group_owner")
    private User groupOwner;
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = {CascadeType.REFRESH}, mappedBy = "group", fetch = FetchType.LAZY)
    private Set<GroupMember> members;
}
