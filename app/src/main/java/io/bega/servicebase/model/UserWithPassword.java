package io.bega.servicebase.model;

import com.google.gson.annotations.SerializedName;

import org.hibernate.validator.constraints.NotBlank;

public class UserWithPassword extends User {


	@SerializedName("password")
	@NotBlank(message = "Please provide a password")
	private final String password;

	public UserWithPassword(String name, String email, String password) {
		super(name, email);

		this.password = password;
	}


}
