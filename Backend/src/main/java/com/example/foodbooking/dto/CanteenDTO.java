package com.example.foodbooking.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CanteenDTO {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	private String username;
	@NotBlank
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

}
