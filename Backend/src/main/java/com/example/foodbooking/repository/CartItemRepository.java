package com.example.foodbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodbooking.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}