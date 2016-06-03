package com.github.corneil.data_rest_demo.data_service.data;

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
public class GroupMember {
    @NotNull
    private Boolean enabled;
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "user_a")
    private User user;
    @NotNull
    @ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "group_a")
    private Group group;

    public GroupMember() {
        super();
    }

    public GroupMember(Group group, User user, Boolean enabled) {
        super();
        this.group = group;
        this.user = user;
        this.enabled = enabled;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GroupMember that = (GroupMember) o;
        if (user != null ? !user.equals(that.user) : that.user != null)
            return false;
        return group != null ? group.equals(that.group) : that.group == null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupMember{");
        sb.append("id=").append(id);
        sb.append(", enabled=").append(enabled);
        if (user != null) {
            sb.append(", user=").append(user.getUserId());
        }
        if (group != null) {
            sb.append(", group=").append(group.getGroupName());
        }
        sb.append('}');
        return sb.toString();
    }
}
