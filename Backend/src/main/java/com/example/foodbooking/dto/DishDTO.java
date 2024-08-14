package com.example.foodbooking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DishDTO {
	@JsonProperty(access = Access.READ_ONLY)
	private Long id;

	private String name;

	private int quantity_remaining;

	private int price;

}
