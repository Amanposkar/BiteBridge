package com.example.foodbooking.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String username;
	private String password;
	private String first_name;
	private String last_name;
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private UserRole role = UserRole.ROLE_ADMIN;

	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Dish> dishes = new ArrayList<Dish>();

	@OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
	Set<Student> students = new HashSet<Student>();

	public void addDish(Dish dish) {
		dish.setAdmin(this);
		dishes.add(dish);
	}

	public void removeDish(Dish dish) {
		dishes.remove(dish);
	}

	public void addStudent(Student student) {
		student.setAdmin(this);
		students.add(student);
	}

	public void removeStudent(Student student) {
		students.remove(student);
	}

}
