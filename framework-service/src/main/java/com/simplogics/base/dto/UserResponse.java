package com.simplogics.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

	private Long id;

	private String name;

	private String email;

	private List<String> roles;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private Boolean active;

}
