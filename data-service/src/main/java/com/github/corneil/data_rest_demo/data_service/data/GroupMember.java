package com.github.corneil.data_rest_demo.data_service.data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private UserInfo member;
    @NotNull
    @ManyToOne(cascade = { CascadeType.REFRESH })
    private GroupInfo memberOfgroup;

    public GroupMember() {
        super();
    }

    public GroupMember(GroupInfo memberOfgroup, UserInfo member, Boolean enabled) {
        super();
        this.memberOfgroup = memberOfgroup;
        this.member = member;
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

    public UserInfo getMember() {
        return member;
    }

    public void setMember(UserInfo member) {
        this.member = member;
    }

    public GroupInfo getMemberOfgroup() {
        return memberOfgroup;
    }

    public void setMemberOfgroup(GroupInfo memberOfgroup) {
        this.memberOfgroup = memberOfgroup;
    }

    @Override
    public int hashCode() {
        int result = member != null ? member.hashCode() : 0;
        result = 31 * result + (memberOfgroup != null ? memberOfgroup.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        GroupMember that = (GroupMember) o;
        if (member != null ? !member.equals(that.member) : that.member != null)
            return false;
        return memberOfgroup != null ? memberOfgroup.equals(that.memberOfgroup) : that.memberOfgroup == null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupMember{");
        sb.append("id=").append(id);
        sb.append(", enabled=").append(enabled);
        if (member != null) {
            sb.append(", member=").append(member.getUserId());
        }
        if (memberOfgroup != null) {
            sb.append(", memberOfgroup=").append(memberOfgroup.getGroupName());
        }
        sb.append('}');
        return sb.toString();
    }
}
