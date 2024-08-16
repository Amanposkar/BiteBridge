package com.example.foodbooking.redis.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	//RedisTemplate is a helper class that simplifies Redis operations, like saving and retrieving data from Redis.
	
	//RedisConnectionFactory= This is an interface that Spring uses to connect to Redis. It abstracts the connection details 
	//so that RedisTemplate can interact with Redis
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new GenericToStringSerializer<>(Object.class));
		template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
		return template;
	}
}
