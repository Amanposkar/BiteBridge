package com.example.foodbooking.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Orders")
public class Orders implements Comparable<Orders> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

//	@ManyToOne
//	@JoinColumn(name = "dish_id", nullable = false)
//	private Dish dish;
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Cart cart;

	@CreationTimestamp
	private LocalDateTime timestamp;

	private boolean is_cancelled = false;

	@Enumerated(value = EnumType.STRING)
	private Status status;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Receipt receipt;

	private String qrcodepath;

	@Override
	public int compareTo(Orders o) {
		return o.getTimestamp().compareTo(this.timestamp);
	}
}
