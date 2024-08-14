package com.example.foodbooking.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptDTO {
	public Long orderId;
	public String studentName;
	public String prn;
	public List<CartItemDTO> dishes = new ArrayList<CartItemDTO>();
	public int totalOrderPrice;

	public void addCartItemDTO(CartItemDTO o) {
		dishes.add(o);
	}
}
