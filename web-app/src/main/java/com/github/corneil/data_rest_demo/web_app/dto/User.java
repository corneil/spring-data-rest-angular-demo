package com.github.corneil.data_rest_demo.web_app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class User {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String emailAddress;
    @NotNull
    private String fullName;
    @NotNull
    private String userId;
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", dateOfBirth=").append(dateOfBirth);
        sb.append('}');
        return sb.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return userId != null ? userId.equals(user.userId) : user.userId == null;
    }
    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
