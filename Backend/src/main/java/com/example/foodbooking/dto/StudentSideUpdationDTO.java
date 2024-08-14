package com.example.foodbooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentSideUpdationDTO {

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	private String first_name;
	private String last_name;
	private String mobileNo;
}
