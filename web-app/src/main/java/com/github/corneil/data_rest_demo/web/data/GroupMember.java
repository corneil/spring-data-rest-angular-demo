package com.github.corneil.data_rest_demo.web.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Corneil du Plessis
 */
@Entity
@Table(name = "group_members", schema = "sd")
@Data
@EqualsAndHashCode(of = {"user", "group"})
public class GroupMember {
	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Boolean enabled;

	@NotNull
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_a")
	private User user;

	@NotNull
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "group_a")
	private Group group;
}
