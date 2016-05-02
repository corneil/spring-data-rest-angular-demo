package com.github.corneil.data_rest_demo.web_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {
    public static class GroupResource extends Resource<Group> {
        public GroupResource() {
            super(new Group());
        }
        public GroupResource(Group content, Iterable<Link> links) {
            super(content, links);
        }
        public GroupResource(Group content, Link... links) {
            super(content, links);
        }
    }
    @NotNull
    private String description;
    @NotNull
    private String groupName;
    @NotNull
    private String groupOwner;
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
    public String getGroupOwner() {
        return groupOwner;
    }
    public void setGroupOwner(String groupOwner) {
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
