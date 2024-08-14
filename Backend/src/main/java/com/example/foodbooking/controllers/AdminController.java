package com.example.foodbooking.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

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
import com.example.foodbooking.dto.AdminDTO;
import com.example.foodbooking.dto.CanteenDTO;
import com.example.foodbooking.dto.DishDTO;
import com.example.foodbooking.dto.SigninResponse;
import com.example.foodbooking.dto.StudentDTO;
import com.example.foodbooking.security.JwtUtils;
import com.example.foodbooking.service.AdminService;
import com.example.foodbooking.service.RedisService;

@RestController
@RequestMapping("/admin")
@Valid
@CrossOrigin
public class AdminController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager mgr;

	@Autowired
	private AdminService adminService;

	@Autowired
	private RedisService redisService;

	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminDTO admin) throws ServerSideException {
		Authentication verifiedAuth = mgr
				.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));
//		adminService.login(admin);
		return ResponseEntity
				.ok(new SigninResponse(jwtUtils.generateJwtToken(verifiedAuth), "Successful Authentication!!!"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add/dish")
	public ResponseEntity<?> addDish(@RequestBody @Valid DishDTO dish) throws ServerSideException {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.addNewDish(dish));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/updatedish/{dishId}/{amount}")
	public ResponseEntity<?> updateAmount(@PathVariable Long dishId, @PathVariable @Min(1) Long amount)
			throws ServerSideException {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.updateDishQuantity(dishId, amount));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/removedish/{dishId}")
	public ResponseEntity<?> deleteDish(@PathVariable Long dishId) throws ServerSideException {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.deleteExistingDish(dishId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add/student")
	public ResponseEntity<?> addStudent(@RequestBody @Valid StudentDTO student) throws ServerSideException {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.addNewStudent(student));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/edit/student/{studentId}")
	public ResponseEntity<?> editStudent(@PathVariable Long studentId, @RequestBody @Valid StudentDTO student)
			throws ServerSideException {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.editPreviousStudent(studentId, student));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/student/{studentId}")
	public ResponseEntity<?> getStudent(@PathVariable Long studentId) throws ServerSideException {
		return ResponseEntity.ok(adminService.getStudentById(studentId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/student/{studentId}")
	public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) throws ServerSideException {
		return ResponseEntity.ok(adminService.deleteExistingStudent(studentId));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/student/{prn}/{amount}")
	public ResponseEntity<?> updateTokenBalance(@PathVariable @Min(50) @Max(10000) @NotEmpty Long amount,
			@PathVariable String prn) throws ServerSideException {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(adminService.updateTokenBalance(prn, amount));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/students")
	public ResponseEntity<?> getAllStudents() {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllStudents());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/dishes")
	public ResponseEntity<?> getAllDishes() {
		return ResponseEntity.ok(adminService.getAllDishes());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/canteens")
	public ResponseEntity<?> getAllCanteen() {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllCanteens());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add/canteen")
	public ResponseEntity<?> addCanteen(@RequestBody @Valid CanteenDTO canteen) throws ServerSideException {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.addNewCanteen(canteen));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/edit/canteen/{canteenId}")
	public ResponseEntity<?> editStudent(@PathVariable Long canteenId, @RequestBody @Valid CanteenDTO canteen)
			throws ServerSideException {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.editPreviousCanteen(canteenId, canteen));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/canteen/{canteenId}")
	public ResponseEntity<?> deleteCanteen(@PathVariable Long canteenId) throws ServerSideException {
		return ResponseEntity.ok(adminService.deleteExistingCanteen(canteenId));
	}

	@PreAuthorize("hasRole('ADMIN')")
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
