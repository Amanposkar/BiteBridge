package com.example.foodbooking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDTO {
	private String dishName;
	private int price;
	private int quantity;
	private int totalPrice;
}
