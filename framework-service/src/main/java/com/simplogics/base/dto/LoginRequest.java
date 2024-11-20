package com.simplogics.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

	@NotBlank(message = "validation.email.empty")
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank(message = "validation.password.empty")
	private String password;

}
