package com.github.corneil.data_rest_demo.data_service.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users", schema = "sd")
@Data
@EqualsAndHashCode(of = {"userId"})
public class User {
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    @Email
    private String emailAddress;
    @NotNull
    private String fullName;
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Column(unique = true)
    private String userId;
}
