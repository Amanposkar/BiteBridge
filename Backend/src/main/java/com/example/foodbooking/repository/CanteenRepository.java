package com.example.foodbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodbooking.entity.Canteen;

public interface CanteenRepository extends JpaRepository<Canteen, Long> {
	Optional<Canteen> findByUsername(String email);
}
