package com.github.corneil.data_rest_demo.web_app.dto;

import javax.validation.constraints.NotNull;

public class Group {
    @NotNull
    private String description;
    @NotNull
    private String groupName;
    @NotNull
    private User groupOwner;
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
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Group{");
        sb.append("groupName='").append(groupName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", groupOwner=").append(groupOwner);
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
    @Override
    public int hashCode() {
        return groupName != null ? groupName.hashCode() : 0;
    }
}
