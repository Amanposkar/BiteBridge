package com.example.foodbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodbooking.entity.Cart;
import com.example.foodbooking.entity.Student;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByStudent(Student student);
}
