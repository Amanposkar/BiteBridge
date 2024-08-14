package com.example.foodbooking.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodbooking.customexception.ServerSideException;
import com.example.foodbooking.dto.CanteenDTO;
import com.example.foodbooking.dto.SigninResponse;
import com.example.foodbooking.security.JwtUtils;
import com.example.foodbooking.service.CanteenService;
import com.example.foodbooking.service.RedisService;

@RestController
@RequestMapping("/canteen")
@CrossOrigin
public class CanteenController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager mgr;

	@Autowired
	private CanteenService canteenService;

	@Autowired
	private RedisService redisService;

	@PreAuthorize("hasRole('CANTEEN')")
	@DeleteMapping("/served/{orderId}")
	public ResponseEntity<?> scanQrToReceipt(@PathVariable Long orderId) throws ServerSideException {
		return ResponseEntity.ok(canteenService.servedOrder(orderId));
	}

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody @Valid CanteenDTO canteen) throws ServerSideException {
		Authentication verifiedAuth = mgr
				.authenticate(new UsernamePasswordAuthenticationToken(canteen.getUsername(), canteen.getPassword()));
//		adminService.login(admin);
		return ResponseEntity
				.ok(new SigninResponse(jwtUtils.generateJwtToken(verifiedAuth), "Successful Authentication!!!"));
	}

	@PreAuthorize("hasRole('CANTEEN')")
	@GetMapping("/getOrder/{orderId}")
	public ResponseEntity<?> getOrder(@PathVariable Long orderId) throws ServerSideException {
		return ResponseEntity.ok(canteenService.getOrderFromId(orderId));
	}

	@PreAuthorize("hasRole('CANTEEN')")
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

}
