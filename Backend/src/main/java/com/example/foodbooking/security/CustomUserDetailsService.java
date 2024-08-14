package com.example.foodbooking.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.foodbooking.entity.Admin;
import com.example.foodbooking.entity.Canteen;
import com.example.foodbooking.entity.Student;
import com.example.foodbooking.repository.AdminRepository;
import com.example.foodbooking.repository.CanteenRepository;
import com.example.foodbooking.repository.StudentRepository;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	// dep : dao layer
	@Autowired
	private AdminRepository adminRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private CanteenRepository canteenRepo;

	@Override
	public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepo.findByUsername(email).orElse(null);
		Student student = studentRepo.findByUsername(email).orElse(null);
		Canteen canteen = canteenRepo.findByUsername(email).orElse(null);
		if (admin == null && student == null && canteen == null)
			throw new UsernameNotFoundException("Email Not Found!!!!");
		else if (admin != null)
			return new CustomUserDetails(admin);
		else if (student != null)
			return new CustomUserDetails(student);
		else
			return new CustomUserDetails(canteen);
	}

}
