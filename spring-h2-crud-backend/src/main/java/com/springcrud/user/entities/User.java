package com.springcrud.user.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Valid
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	
	@NotNull(message="name cannot be blank")
	@Size(min = 2, message = "Name must be at least 2 characters long")
	@Pattern(regexp = "[A-Za-z]+", message = "Name must not contain numbers")
	//@Pattern(regexp = "^[A-Za-z.-]+(\\s*[A-Za-z.-]+)*$", message = "Name must not contain number") // accetta tutto eccetto numeri
	private String name;

	
	@NotNull(message="email cannot be blank")
	@Column(unique=true)
	@Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\." + "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+ "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9]"
			+ "(?:[A-Za-z0-9-]*[A-Za-z0-9])?", message = "Email format not valid")
	private String email;

	public User() {
		this.name = "";
		this.email = "";
	}

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", name=" + name + ", email=" + email + '}';
	}
}
