package com.example.foodbooking.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Check;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Check(constraints = "quantity_remaining > -1")
public class Dish {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	private int quantity_remaining;

	private int price;

//	@OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
//	private Set<Orders> orders;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartItem> cartItems = new ArrayList<CartItem>();

//	public void addOrder(Orders order) {
//		order.setDish(this);
//		orders.add(order);
//	}
//
//	public void removeOrder(Orders order) {
//		orders.remove(order);
//	}

	public void addToCartItem(CartItem cartItem) {
		cartItem.setDish(this);
		cartItems.add(cartItem);
	}

	public void removeFromCartItem(CartItem cartItem) {
		cartItems.remove(cartItem);
	}

}
