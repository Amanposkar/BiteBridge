package com.example.foodbooking.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cart cart;

	@ManyToOne
	private Dish dish;

	private int quantity;

	@Column(name = "total_price")
	private int totalPrice;

}
