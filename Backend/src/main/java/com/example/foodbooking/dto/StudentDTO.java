package com.example.foodbooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDTO {

	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	private long tokens_balance;
	private String first_name;
	private String last_name;
	private String prn;
	private String mobileNo;
	// private Long dishId;
	// private int quantity;
	// @JsonProperty(access = Access.WRITE_ONLY)
	// private Long friendId;

}
