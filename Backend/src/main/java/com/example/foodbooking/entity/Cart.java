package com.example.foodbooking.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Student student;

	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> items = new ArrayList<CartItem>();

	@Enumerated(EnumType.STRING)
	private StatusCart status = StatusCart.ACTIVE;

	@OneToOne
	@JoinColumn(name = "order_id")
	private Orders order;

	@Column(name = "orderpricing")
	private int total;

	public void addCartItemToCart(CartItem item) {
		item.setCart(this);
		items.add(item);
	}

	public void deleteCartItemFromCart(CartItem item) {
		items.remove(item);
	}
}