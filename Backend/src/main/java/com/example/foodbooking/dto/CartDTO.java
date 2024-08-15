package com.example.foodbooking.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.foodbooking.entity.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CartDTO {
	private Long orderId;
	private Status orderStatus;
	private List<CartItemDTO> cartItemDTO = new ArrayList<CartItemDTO>();
	private LocalDateTime timeStamp;
	private int totalCartPrice;

	public void addItemInList(CartItemDTO o) {
		cartItemDTO.add(o);
	}
}
