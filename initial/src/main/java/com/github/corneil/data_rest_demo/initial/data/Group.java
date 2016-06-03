package com.github.corneil.data_rest_demo.initial.data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "groups", schema = "sd")
public class Group {
    @NotNull
    private String description;
    @NotNull
    @Column(unique = true)
    private String groupName;
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    private User groupOwner;
    @Id
    @GeneratedValue
    private Long id;
    public Group() {
        super();
    }
    public Group(String groupName, String description, User groupOwner) {
        super();
        this.groupName = groupName;
        this.description = description;
        this.groupOwner = groupOwner;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public User getGroupOwner() {
        return groupOwner;
    }
    public void setGroupOwner(User groupOwner) {
        this.groupOwner = groupOwner;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public int hashCode() {
        return groupName != null ? groupName.hashCode() : 0;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Group{");
        sb.append("id=").append(id);
        sb.append(", groupName='").append(groupName).append('\'');
        sb.append(", description='").append(description).append('\'');
        if (groupOwner != null) {
            sb.append(", groupOwner=").append(groupOwner.getUserId());
        }
        sb.append('}');
        return sb.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Group group = (Group) o;
        return groupName != null ? groupName.equals(group.groupName) : group.groupName == null;
    }
}
