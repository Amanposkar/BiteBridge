package com.example.foodbooking.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static final String BLACKLIST_KEY = "blacklistedTokens";

	public void blacklistToken(String token, long expirationTime) {
		redisTemplate.opsForHash().put(BLACKLIST_KEY, token, expirationTime);
	}

	public boolean isTokenBlacklisted(String token) {
		return redisTemplate.opsForHash().hasKey(BLACKLIST_KEY, token);
	}

	public void removeExpiredTokens() {
		Map<Object, Object> blacklistedTokens = redisTemplate.opsForHash().entries(BLACKLIST_KEY);
		long currentTime = System.currentTimeMillis();

		for (Map.Entry<Object, Object> entry : blacklistedTokens.entrySet()) {
			long expirationTime = (long) entry.getValue();
			if (currentTime > expirationTime) {
				redisTemplate.opsForHash().delete(BLACKLIST_KEY, entry.getKey());
			}
		}
	}
}