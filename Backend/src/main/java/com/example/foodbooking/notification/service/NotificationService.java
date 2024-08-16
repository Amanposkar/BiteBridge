package com.example.foodbooking.notification.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

	private String uri;

	public NotificationService() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("ngrok_url.txt"), StandardCharsets.UTF_16))) {
			String line = reader.readLine();
			if (line != null) {
				uri = line.trim(); // .trim() to remove any leading or trailing whitespace
				System.out.println("Read URL: " + uri);
			} else {
				System.err.println("Line is null or empty.");
			}
		} catch (IOException e) {
			System.err.println("Error reading ngrok URL file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void sendWhatsAppMessage(String to, String body, Orders order, boolean b) {
		String path = uri + "/" + order.getStudent().getId() + "/" + order.getStudent().getPrn() + "_" + order.getId()
				+ "_QRCODE.png";
		Message message = Message.creator(new PhoneNumber("whatsapp:" + to),
				new PhoneNumber("whatsapp:" + twilioConfiguration.getPhoneNumber()),
				body + (b ? ", Click on link to generate QRcode:\n" + path : "")).create();
		System.out.println(message.getSid());
	}
}