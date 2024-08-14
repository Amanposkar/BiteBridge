package com.example.foodbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodbooking.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
	Optional<Student> findByUsernameAndPassword(String username, String password);

	Optional<Student> findByPrn(String prn);

	Optional<Student> findByUsername(String email);
}
