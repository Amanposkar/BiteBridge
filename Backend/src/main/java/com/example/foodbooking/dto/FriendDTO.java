package com.example.foodbooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendDTO {
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	private String first_name;
	private String last_name;
	private String prn;
}
