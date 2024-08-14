package com.example.foodbooking.customexception;

public class ServerSideException extends Exception {

	private static final long serialVersionUID = 1L;

	public ServerSideException(String errorMessage) {
		super(errorMessage);
	}
}
