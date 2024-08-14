package com.example.foodbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodbooking.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByUsernameAndPassword(String userName, String password);

	Optional<Admin> findByUsername(String email);
}
