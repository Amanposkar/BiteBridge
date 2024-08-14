package com.example.foodbooking.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
	private String message;
	private LocalDateTime timeStamp;

	public ApiResponse(String message) {
		this.message = message;
		this.timeStamp = LocalDateTime.now();
	}
}
