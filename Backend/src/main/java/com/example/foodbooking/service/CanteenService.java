package com.example.foodbooking.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foodbooking.customexception.ServerSideException;
import com.example.foodbooking.dto.ApiResponse;
import com.example.foodbooking.dto.CartItemDTO;
import com.example.foodbooking.dto.ReceiptDTO;
import com.example.foodbooking.entity.Orders;
import com.example.foodbooking.entity.Status;
import com.example.foodbooking.entity.StatusCart;
import com.example.foodbooking.repository.OrdersRepository;
import com.example.foodbooking.utils.QRCodeGenerator;
import com.example.foodbooking.entity.CartItem;
import com.example.foodbooking.entity.Dish;

@Service
@Transactional
public class CanteenService {

	@Autowired
	private OrdersRepository ordersRepository;

	public ApiResponse servedOrder(Long orderId) throws ServerSideException {
		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new ServerSideException("Order with such Order Id doesn't exists"));
		order.setStatus(Status.SERVED);
		QRCodeGenerator.deleteQRCode(order);
		return new ApiResponse("Order Served Successfully");
	}

	public Object getOrderFromId(Long orderId) throws ServerSideException {
		Orders order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new ServerSideException("Order with such Order Id doesn't exists"));
		if (order.getStatus().equals(Status.PLACED)) {
			order.getCart().setStatus(StatusCart.INACTIVE);
			ReceiptDTO receipt = new ReceiptDTO();
			receipt.setOrderId(orderId);
			receipt.setStudentName(order.getStudent().getFirst_name() + " " + order.getStudent().getLast_name());
			receipt.setPrn(order.getStudent().getPrn());

			Dish dish;
			for (CartItem cartItem : order.getCart().getItems()) {
				CartItemDTO cartItemDTO = new CartItemDTO();
				dish = cartItem.getDish();
				cartItemDTO.setDishName(dish.getName());
				cartItemDTO.setPrice(dish.getPrice());
				cartItemDTO.setQuantity(cartItem.getQuantity());
				cartItemDTO.setTotalPrice(cartItem.getTotalPrice());
				receipt.addCartItemDTO(cartItemDTO);
			}
			receipt.setTotalOrderPrice(order.getCart().getTotal());
			return receipt;

		} else if (order.getStatus().equals(Status.SERVED)) {
			return new ApiResponse("Order With Given Order Id : " + orderId + " has already been Served");
		} else {
			return new ApiResponse("Order With Given Order Id : " + orderId + " has been Cancelled");
		}
	}

}
