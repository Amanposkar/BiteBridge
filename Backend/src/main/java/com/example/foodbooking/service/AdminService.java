package com.example.foodbooking.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.foodbooking.customexception.ServerSideException;
import com.example.foodbooking.dto.ApiResponse;
import com.example.foodbooking.dto.CanteenDTO;
import com.example.foodbooking.dto.DishDTO;
import com.example.foodbooking.dto.StudentDTO;
import com.example.foodbooking.entity.Admin;
import com.example.foodbooking.entity.Canteen;
import com.example.foodbooking.entity.Dish;
import com.example.foodbooking.entity.Student;
import com.example.foodbooking.repository.AdminRepository;
import com.example.foodbooking.repository.CanteenRepository;
import com.example.foodbooking.repository.DishRepository;
import com.example.foodbooking.repository.StudentRepository;
import com.example.foodbooking.security.CustomUserDetails;

@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private DishRepository dishReposistory;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CanteenRepository canteenRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder encoder;

	public Long processUserDetails() {
		CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Long.parseLong(principal.getId());
	}

//	public ApiResponse login(AdminDTO user) throws ServerSideException {
//		Admin admin = adminRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword())
//				.orElseThrow(() -> new ServerSideException("No such Admin exists with given Username"));
//		System.out.println(admin.getId());
//		return new ApiResponse("UserName Found Successfully");
//	}

	public DishDTO addNewDish(@Valid DishDTO dish) throws ServerSideException {
		Admin adminLogin = adminRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Admin exists with given UserId"));
		Dish addedDish = mapper.map(dish, Dish.class);
		adminLogin.addDish(addedDish);
		mapper.map(dishReposistory.save(addedDish), dish);

		return dish;
	}

	public ApiResponse deleteExistingDish(Long id) throws ServerSideException {
		Admin adminLogin = adminRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Admin exists with given UserId"));
		if (!dishReposistory.existsById(id))
			return new ApiResponse("No Such Dish Present");
		Dish dish = dishReposistory.findById(id)
				.orElseThrow(() -> new ServerSideException("No Such Dish exists with given DishId"));
		adminLogin.removeDish(dish);
		return new ApiResponse("Dish Deleted Successfully");
	}

	public ApiResponse addNewStudent(StudentDTO student) throws ServerSideException {
		Admin adminLogin = adminRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Admin exists with given UserId"));
		Student newStudent = mapper.map(student, Student.class);
		newStudent.setPassword(encoder.encode(newStudent.getPassword()));
		adminLogin.addStudent(newStudent);
		return new ApiResponse("Student Added Successfully");
	}

	public ApiResponse editPreviousStudent(Long id, StudentDTO student) throws ServerSideException {
		Student oldStudent = studentRepository.findById(id)
				.orElseThrow(() -> new ServerSideException("No such Student present with given StudentId"));
		student.setPassword(encoder.encode(student.getPassword()));
		mapper.map(student, oldStudent);
		return new ApiResponse("Student Details edited successfully");
	}

	public StudentDTO getStudentById(Long id) throws ServerSideException {
		return mapper.map(
				studentRepository.findById(id)
						.orElseThrow(() -> new ServerSideException("No such Student present with given StudentId")),
				StudentDTO.class);
	}

	public ApiResponse deleteExistingStudent(Long id) throws ServerSideException {
		Admin adminLogin = adminRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Admin exists with given UserId"));
		adminLogin.removeStudent(studentRepository.findById(id)
				.orElseThrow(() -> new ServerSideException("No such Student present with given StudentId")));
		return new ApiResponse("Student Deleted Successfully");
	}

	public ApiResponse updateTokenBalance(String prn, Long amount) throws ServerSideException {
		if (amount > 50 && amount < 10000) {
			Student student = studentRepository.findByPrn(prn)
					.orElseThrow(() -> new ServerSideException("No such Student present with given StudentId"));
			student.setTokens_balance(student.getTokens_balance() + amount);
		} else
			return new ApiResponse("Balance Limits Crossed {Should be Between 50 and 10000}");
		return new ApiResponse("Balance Updated Successfully");
	}

	public List<StudentDTO> getAllStudents() {
		return studentRepository.findAll().stream().map(student -> mapper.map(student, StudentDTO.class))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}

	public List<DishDTO> getAllDishes() {
		return dishReposistory.findAll().stream().map(dish -> mapper.map(dish, DishDTO.class)).collect(ArrayList::new,
				ArrayList::add, ArrayList::addAll);
	}

	public ApiResponse updateDishQuantity(Long dishId, @Min(1) Long amount) throws ServerSideException {
		Dish dish = dishReposistory.findById(dishId)
				.orElseThrow(() -> new ServerSideException("No such dish present with given DishId"));
		dish.setQuantity_remaining(dish.getQuantity_remaining() + (int) (long) amount);
		return new ApiResponse("Dish Quantity Updated Successfully");
	}

	public List<CanteenDTO> getAllCanteens() {
		return canteenRepository.findAll().stream().map(canteen -> mapper.map(canteen, CanteenDTO.class))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}

	public ApiResponse addNewCanteen(@Valid CanteenDTO canteen) {
		canteen.setPassword(encoder.encode(canteen.getPassword()));
		canteenRepository.save(mapper.map(canteen, Canteen.class));
		return new ApiResponse("Canteen Login Added Successfully");
	}

	public ApiResponse editPreviousCanteen(Long canteenId, CanteenDTO canteen) throws ServerSideException {
		Canteen oldCanteenDetails = canteenRepository.findById(canteenId)
				.orElseThrow(() -> new ServerSideException("No such Canteen present with given CanteenId"));
		canteen.setPassword(encoder.encode(canteen.getPassword()));
		mapper.map(canteen, oldCanteenDetails);
		return new ApiResponse("Canteen Login Details Updated Successfully");
	}

	public ApiResponse deleteExistingCanteen(Long canteenId) throws ServerSideException {
		Canteen oldCanteenDetails = canteenRepository.findById(canteenId)
				.orElseThrow(() -> new ServerSideException("No such Canteen present with given CanteenId"));
		canteenRepository.delete(oldCanteenDetails);
		return new ApiResponse("Canteen Login Deleted Successfully");
	}

}
