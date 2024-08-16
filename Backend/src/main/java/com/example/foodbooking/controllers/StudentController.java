package com.example.foodbooking.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodbooking.customexception.ServerSideException;
import com.example.foodbooking.dto.OrdersDTO;
import com.example.foodbooking.dto.SigninResponse;
import com.example.foodbooking.dto.StudentLoginDTO;
import com.example.foodbooking.dto.StudentSideUpdationDTO;
import com.example.foodbooking.security.JwtUtils;
import com.example.foodbooking.service.RedisService;
import com.example.foodbooking.service.StudentService;
import com.google.zxing.WriterException;

@RestController
@Valid
@CrossOrigin
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager mgr;

	@Autowired
	private StudentService studentService;

	@Autowired
	private RedisService redisService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid StudentLoginDTO studentLoginDTO) throws ServerSideException {
		Authentication verifiedAuth = mgr.authenticate(
				new UsernamePasswordAuthenticationToken(studentLoginDTO.getUsername(), studentLoginDTO.getPassword()));
//		studentService.login(studentLoginDTO);
		return ResponseEntity.ok(
				new SigninResponse(jwtUtils.generateJwtToken(verifiedAuth), "Successful Student Authentication!!!"));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/studentdetails")
	public ResponseEntity<?> getStudentDetails() throws ServerSideException {
		return ResponseEntity.ok(studentService.getStudentDetails());
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/dishes")
	public ResponseEntity<?> getAllDishes() {
		return ResponseEntity.ok(studentService.getAllDishes());
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PostMapping("/order")
	public ResponseEntity<?> placeOrder(@RequestBody @Valid List<OrdersDTO> order)
			throws ServerSideException, WriterException, IOException {
		return ResponseEntity.ok(studentService.placeOrder(order));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/orders")
	public ResponseEntity<?> getAllOrders() throws ServerSideException {
		return ResponseEntity.ok(studentService.getAllOrders());
	}

	@PreAuthorize("hasRole('STUDENT')")
	@DeleteMapping("/orders/{orderId}")
	public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) throws ServerSideException {
		return ResponseEntity.ok(studentService.deleteParticularOrder(orderId));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PutMapping("/delegate/{orderId}/{friendId}")
	public ResponseEntity<?> delegateOrder(@PathVariable Long orderId, @PathVariable Long friendId)
			throws ServerSideException, WriterException, IOException {
		return ResponseEntity.ok(studentService.delegateOrder(orderId, friendId));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/friends")
	public ResponseEntity<?> getFriends() throws ServerSideException {
		return ResponseEntity.ok(studentService.getAllFriends());
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PostMapping("/friend/{prn}")
	public ResponseEntity<?> addFriend(@PathVariable String prn) throws ServerSideException {
		return ResponseEntity.ok(studentService.addNewFriend(prn));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@DeleteMapping("/friend/{friendId}")
	public ResponseEntity<?> deleteFriend(@PathVariable Long friendId) throws ServerSideException {
		return ResponseEntity.ok(studentService.deleteFriend(friendId));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PatchMapping("/edit")
	public ResponseEntity<?> editStudent(@RequestBody @Valid StudentSideUpdationDTO student)
			throws ServerSideException {
		return ResponseEntity.status(HttpStatus.CREATED).body(studentService.editStudentDetails(student));
	}

	@PreAuthorize("hasRole('STUDENT')")
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
		// Extract the JWT token from the "Bearer" string
		String jwtToken = token.substring(7);

		// Get the expiration time from the token
		long expirationTime = jwtUtils.getExpirationDateFromToken(jwtToken).getTime();

		// Add the token to the blacklist with its expiration time
		redisService.blacklistToken(jwtToken, expirationTime);

		return ResponseEntity.ok("Logged out successfully.");
	}

	@PreAuthorize("hasRole('STUDENT')")
	@GetMapping("/history")
	public ResponseEntity<?> getHistory() throws ServerSideException {
		return ResponseEntity.ok(studentService.getHistory());
	}

}
