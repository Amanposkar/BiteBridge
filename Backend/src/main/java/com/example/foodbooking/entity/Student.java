package com.example.foodbooking.entity;

import java.util.ArrayList;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String username;
	private String password;
	private Long tokens_balance;
	private String first_name;
	private String last_name;
	@Column(unique = true)
	private String prn;
	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private UserRole role = UserRole.ROLE_STUDENT;
	@OneToMany(mappedBy = "student")
	private Set<Orders> orders;

	@Column(name = "mobile_no", length = 20)
	private String mobileNo;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "FriendList", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "friend_student_id"))
	private Set<Student> friends;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cart> cart = new ArrayList<Cart>();

	public void addCart(Cart cartPop) {
		cartPop.setStudent(this);
		cart.add(cartPop);
	}

	public void deleteCart(Cart cartPop) {
		cart.remove(cartPop);
	}

	public void addOrders(Orders order) {
		order.setStudent(this);
		orders.add(order);
	}

	public void deleteOrder(Orders order) {
		orders.remove(order);
	}

	public void addFriend(Student student) {
		friends.add(student);
	}

	public void removeFriend(Student student) {
		friends.remove(student);
	}

	@Override
	public boolean equals(Object o) {
		Student student = (Student) o;
		return this.getPrn().equals(student.getPrn());
	}

	public boolean isFriend(String prn) {
		for (Student student : friends) {
			if (student.getPrn().equals(prn))
				return true;
		}
		return false;
	}
}
