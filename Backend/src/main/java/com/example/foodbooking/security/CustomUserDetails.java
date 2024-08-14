package com.example.foodbooking.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.foodbooking.entity.Admin;
import com.example.foodbooking.entity.Canteen;
import com.example.foodbooking.entity.Student;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Object user;

	public CustomUserDetails(Object user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// class : SimpleGrantedAuthority --> GrantedAuthority
		if (user instanceof Admin)
			return List.of(new SimpleGrantedAuthority(((Admin) user).getRole().toString()));
		else if (user instanceof Student)
			return List.of(new SimpleGrantedAuthority(((Student) user).getRole().toString()));
		else
			return List.of(new SimpleGrantedAuthority(((Canteen) user).getRole().toString()));
	}

	public String getId() {
		if (user instanceof Admin)
			return (((Admin) user).getId()).toString();
		else if (user instanceof Student)
			return (((Student) user).getId()).toString();
		else
			return (((Canteen) user).getId()).toString();
	}

	@Override
	public String getPassword() {
		if (user instanceof Admin)
			return ((Admin) user).getPassword();
		else if (user instanceof Student)
			return ((Student) user).getPassword();
		else
			return ((Canteen) user).getPassword();
	}

	@Override
	public String getUsername() {
		if (user instanceof Admin)
			return ((Admin) user).getUsername();
		else if (user instanceof Student)
			return ((Student) user).getUsername();
		else
			return ((Canteen) user).getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
