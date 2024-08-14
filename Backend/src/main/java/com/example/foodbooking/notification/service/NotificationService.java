package com.example.foodbooking.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foodbooking.entity.Orders;
import com.example.foodbooking.notification.config.TwilioConfiguration;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationService {

	@Autowired
	private TwilioConfiguration twilioConfiguration;

	private String uri = "https://8a23-103-174-141-242.ngrok-free.app/";

	public void sendWhatsAppMessage(String to, String body, Orders order, boolean b) {
		String path = uri + order.getStudent().getId() + "/" + order.getStudent().getPrn() + "_" + order.getId()
				+ "_QRCODE.png";
		Message message = Message.creator(new PhoneNumber("whatsapp:" + to),
				new PhoneNumber("whatsapp:" + twilioConfiguration.getPhoneNumber()),
				body + (b ? ", Click on link to generate QRcode:\n" + path : "")).create();
		System.out.println(message.getSid());
	}
}