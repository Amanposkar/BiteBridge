package com.example.foodbooking.notification.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TwilioConfiguration {

	@Value("${twilio.account_sid}")
	private String accountSid;

	@Value("${twilio.auth_token}")
	private String authToken;

	@Value("${twilio.phone_number}")
	private String phoneNumber;

	@PostConstruct
	public void init() {
		System.out.println("Twilio Account SID: " + accountSid); // For debugging only
		System.out.println("Twilio Auth Token: " + authToken); 
		System.out.println("Twilio Phone No: " + phoneNumber);
		Twilio.init(accountSid, authToken);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}
