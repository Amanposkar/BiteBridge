package com.example.foodbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BlacklistCleanupService {

	@Autowired
	private RedisService redisService;

	@Scheduled(fixedRate = 86400000)
	public void cleanUpBlacklist() {
		redisService.removeExpiredTokens();
	}
}
