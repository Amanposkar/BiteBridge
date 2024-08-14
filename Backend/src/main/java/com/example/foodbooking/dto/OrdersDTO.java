package com.example.foodbooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdersDTO {
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long dishId;
	@JsonProperty(access = Access.READ_ONLY)
	private String dish;
	private int quantity;
	@JsonProperty(access = Access.READ_ONLY)
	private String status;

}
