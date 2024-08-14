package com.example.foodbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodbooking.entity.Orders;
import com.example.foodbooking.entity.Student;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
	List<Orders> findOrdersByStudent(Student student);
}
