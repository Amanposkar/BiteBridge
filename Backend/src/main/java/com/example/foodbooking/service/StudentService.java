package com.example.foodbooking.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.foodbooking.customexception.ServerSideException;
import com.example.foodbooking.dto.ApiResponse;
import com.example.foodbooking.dto.CartDTO;
import com.example.foodbooking.dto.CartItemDTO;
import com.example.foodbooking.dto.DishDTO;
import com.example.foodbooking.dto.FriendDTO;
import com.example.foodbooking.dto.OrdersDTO;
import com.example.foodbooking.dto.StudentDTO;
import com.example.foodbooking.dto.StudentSideUpdationDTO;
import com.example.foodbooking.entity.Cart;
import com.example.foodbooking.entity.CartItem;
import com.example.foodbooking.entity.Dish;
import com.example.foodbooking.entity.Orders;
import com.example.foodbooking.entity.Receipt;
import com.example.foodbooking.entity.Status;
import com.example.foodbooking.entity.StatusCart;
import com.example.foodbooking.entity.Student;
import com.example.foodbooking.notification.service.NotificationService;
import com.example.foodbooking.repository.CartItemRepository;
import com.example.foodbooking.repository.CartRepository;
import com.example.foodbooking.repository.DishRepository;
import com.example.foodbooking.repository.OrdersRepository;
import com.example.foodbooking.repository.ReceiptRepository;
import com.example.foodbooking.repository.StudentRepository;
import com.example.foodbooking.security.CustomUserDetails;
import com.example.foodbooking.utils.QRCodeGenerator;
import com.google.zxing.WriterException;

@Transactional
@Service
public class StudentService {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private DishRepository dishReposistory;

	@Autowired
	private ReceiptRepository receiptRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private PasswordEncoder encoder;

	public StudentDTO getStudentDetails() throws ServerSideException {
		return mapper.map(studentRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No Student with given Id Present")), StudentDTO.class);
	}

	public Long processUserDetails() {
		CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Long.parseLong(principal.getId());
	}

	public List<DishDTO> getAllDishes() {
		return dishReposistory.findAll().stream().filter(dish -> dish.getQuantity_remaining() > 0)
				.map(dish -> mapper.map(dish, DishDTO.class))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}

	public ApiResponse placeOrder(List<OrdersDTO> orders) throws ServerSideException, WriterException, IOException {

		Long id = processUserDetails();
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));

		Cart cart = new Cart();

		int totalCartAmount = 0;
		List<Dish> dishes = new ArrayList<Dish>();
		List<Integer> quantity = new ArrayList<Integer>();

		for (OrdersDTO order : orders) {
			CartItem cartItem = new CartItem();
			Dish dish = dishReposistory.findById(order.getDishId())
					.orElseThrow(() -> new ServerSideException("No such Dish exists with given DishId"));
			if (dish.getQuantity_remaining() <= 0)
				return new ApiResponse("Dish " + dish.getName() + " is not available Right Now");
			if (order.getQuantity() > dish.getQuantity_remaining())
				return new ApiResponse("Your Order quantity exceeds remaining quantity for " + dish.getName());
			int totalAmount = dish.getPrice() * order.getQuantity();
			totalCartAmount += totalAmount;
			if (totalCartAmount > student.getTokens_balance())
				return new ApiResponse("Not Enough Balance Available");

			cartItem.setQuantity(order.getQuantity());
			cartItem.setTotalPrice(totalAmount);

			dish.addToCartItem(cartItem);
			cart.addCartItemToCart(cartItem);

			dishes.add(dish);
			quantity.add(order.getQuantity());

		}

		cart.setTotal(totalCartAmount);
		student.addCart(cart);

		Orders placeNewOrder = new Orders();
		placeNewOrder.setStudent(student);
		placeNewOrder.setCart(cart);
		placeNewOrder.setStatus(Status.PLACED);

		int i = 0;
		for (Dish dish : dishes) {
			dish.setQuantity_remaining(dish.getQuantity_remaining() - quantity.get(i++));
		}

		student.setTokens_balance(student.getTokens_balance() - totalCartAmount);

		student.addOrders(placeNewOrder);

		Receipt receipt = new Receipt(placeNewOrder);
		receipt.setTotal((long) totalCartAmount);
		receiptRepository.save(receipt);
		placeNewOrder.setReceipt(receipt);

		cart.setOrder(placeNewOrder);
		cartRepository.save(cart);
		placeNewOrder = ordersRepository.save(placeNewOrder);
		placeNewOrder.setQrcodepath(QRCodeGenerator.getPath(placeNewOrder));
		System.out.println("Order ID: " + placeNewOrder.getId() + "\n" + "Time: " + placeNewOrder.getTimestamp());
		QRCodeGenerator.generateQRCode(placeNewOrder, receipt.getTotal());
		String message = "Your order has been placed successfully. Order ID: " + placeNewOrder.getId();
		notificationService.sendWhatsAppMessage(student.getMobileNo(), message, placeNewOrder, true);
		return new ApiResponse("Order Placed Successfully");
	}

	public Object getAllOrders() throws ServerSideException {
		Long studentId = processUserDetails();
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		List<Orders> orders = ordersRepository.findOrdersByStudent(student);

		List<CartDTO> cartDTO = new ArrayList<CartDTO>();

		orders.stream().filter(
				values -> !values.getStatus().equals(Status.SERVED) && !values.getStatus().equals(Status.CANCELLED))
				.forEach(values -> {
					System.out.println(values.getStatus());
					Cart cart = values.getCart();
					CartDTO singleCartDTO = new CartDTO();
					cart.getItems().forEach(item -> {
						CartItemDTO cartItemDTO = new CartItemDTO();
						cartItemDTO.setDishName(item.getDish().getName());
						cartItemDTO.setPrice(item.getDish().getPrice());
						cartItemDTO.setQuantity(item.getQuantity());
						cartItemDTO.setTotalPrice(item.getTotalPrice());
						singleCartDTO.addItemInList(cartItemDTO);
					});
					singleCartDTO.setOrderId(values.getId());
					singleCartDTO.setOrderStatus(values.getStatus());
					singleCartDTO.setTotalCartPrice(cart.getTotal());
					singleCartDTO.setTimeStamp(values.getTimestamp());
					cartDTO.add(singleCartDTO);
				});

//		cartDTO.forEach(cart-> System.out.println(cart));

		if (cartDTO.size() > 0)
			return cartDTO;
		else
			return new ApiResponse("Currently No Orders Are Placed !!!");
	}

	public ApiResponse deleteParticularOrder(Long orderId) throws ServerSideException {
		Orders orders = ordersRepository.findById(orderId)
				.orElseThrow(() -> new ServerSideException("No such Order exists with given OrderId"));
		orders.set_cancelled(true);
		orders.setStatus(Status.CANCELLED);
		QRCodeGenerator.deleteQRCode(orders);

		List<CartItem> cartItems = new ArrayList<CartItem>();
		int totalAmount = 0;
		cartItems = orders.getCart().getItems();
		cartItems.forEach(cartItem -> {
			cartItem.getDish()
					.setQuantity_remaining(cartItem.getDish().getQuantity_remaining() + cartItem.getQuantity());
		});

		orders.getCart().setStatus(StatusCart.INACTIVE);
		orders.getStudent().setTokens_balance(orders.getStudent().getTokens_balance() + orders.getCart().getTotal());
		return new ApiResponse("Order Deleted Successfully");
	}

	public List<FriendDTO> getAllFriends() throws ServerSideException {
		Student student = studentRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		return student.getFriends().stream().map(values -> mapper.map(values, FriendDTO.class)).collect(ArrayList::new,
				ArrayList::add, ArrayList::addAll);
	}

	public ApiResponse addNewFriend(String prn) throws ServerSideException {
		Student student = studentRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		Student friend = studentRepository.findByPrn(prn)
				.orElseThrow(() -> new ServerSideException("No such Student exists with given Prn"));
		if (student.getId().equals(friend.getId()))
			return new ApiResponse("Sorry You Can't be your own friend");
		if (student.isFriend(prn)) {
			return new ApiResponse("Given Prn is already a friend");
		}
		student.addFriend(friend);
		return new ApiResponse("Friend Added Successfully");
	}

	public ApiResponse deleteFriend(Long friendId) throws ServerSideException {
		Student student = studentRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		Student friend = studentRepository.findById(friendId)
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		student.removeFriend(friend);
		return new ApiResponse("Friend Deleted Successfully");
	}

	public ApiResponse delegateOrder(Long orderId, Long friendId)
			throws ServerSideException, WriterException, IOException {
		Student student = studentRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		Student friend = studentRepository.findById(friendId)
				.orElseThrow(() -> new ServerSideException("No such Friend exists with given UserId"));
		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new ServerSideException("No such Order exists with given OrderId"));
		QRCodeGenerator.deleteQRCode(order);
		student.deleteOrder(order);
		friend.addOrders(order);
		Receipt receipt = order.getReceipt();
		order.setQrcodepath(QRCodeGenerator.getPath(order));
		QRCodeGenerator.generateQRCode(order, receipt.getTotal());
		String message = "Your order has been placed Delegated. Order ID: " + order.getId();
		notificationService.sendWhatsAppMessage(student.getMobileNo(), message, order, false);
		notificationService.sendWhatsAppMessage(friend.getMobileNo(), message, order, true);
		return new ApiResponse("Order Delegated Successfully");
	}

	public ApiResponse editStudentDetails(@Valid StudentSideUpdationDTO student) throws ServerSideException {
		Student oldStudentDetails = studentRepository.findById(processUserDetails())
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		student.setPassword(encoder.encode(student.getPassword()));
		mapper.map(student, oldStudentDetails);
		return new ApiResponse("Student Details Updated Successfully");
	}

	public Object getHistory() throws ServerSideException {
		Long studentId = processUserDetails();
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ServerSideException("No such Student exists with given UserId"));
		List<Orders> orders = ordersRepository.findOrdersByStudent(student);
		Collections.sort(orders);

		List<CartDTO> cartDTO = new ArrayList<CartDTO>();

		orders.stream().forEach(values -> {
			System.out.println(values.getStatus());
			Cart cart = values.getCart();
			CartDTO singleCartDTO = new CartDTO();
			cart.getItems().forEach(item -> {
				CartItemDTO cartItemDTO = new CartItemDTO();
				cartItemDTO.setDishName(item.getDish().getName());
				cartItemDTO.setPrice(item.getDish().getPrice());
				cartItemDTO.setQuantity(item.getQuantity());
				cartItemDTO.setTotalPrice(item.getTotalPrice());
				singleCartDTO.addItemInList(cartItemDTO);
			});
			singleCartDTO.setOrderId(values.getId());
			singleCartDTO.setOrderStatus(values.getStatus());
			singleCartDTO.setTotalCartPrice(cart.getTotal());
			singleCartDTO.setTimeStamp(values.getTimestamp());
			cartDTO.add(singleCartDTO);
		});

//		cartDTO.forEach(cart-> System.out.println(cart));

		if (cartDTO.size() > 0)
			return cartDTO;
		else
			return new ApiResponse("Currently No Order History is Available !!!");
	}
}
