package com.github.corneil.data_rest_demo.web.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Corneil du Plessis
 */
@Entity
@Table(name = "users", schema = "sd")
@Data
@EqualsAndHashCode(of = {"userId"})
public class User {
	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateOfBirth;

	@Email
	private String emailAddress;

	@NotNull
	private String fullName;

	@NotNull
	@Column(unique = true)
	private String userId;

	@Transient
	private boolean hasImage;
}
