package com.example.foodbooking.security;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

//This marks the class as a Spring bean, meaning it can be automatically injected into other parts of the application.
@Component
//This annotation is from Lombok and provides a logger (log) for the class, which is used to log messages.
@Slf4j
public class JwtUtils {

	@Value("${SECRET_KEY}")
	private String jwtSecret;

	@Value("${EXP_TIMEOUT}")
	private int jwtExpirationMs;

	private Key key;

	//@PostConstruct public void init(): This method runs after the class is initialized by Spring. 
	//It converts the jwtSecret into a Key object using HMAC with SHA-512, which is later used to sign the JWT.
	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	// will be invoked by User controller : signin ) , upon successful
	// authentication
	public String generateJwtToken(Authentication authentication) {
		log.info("generate jwt token " + authentication);
		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
//JWT : userName,issued at ,exp date,digital signature(does not typically contain password , can contain authorities
		return Jwts.builder() // JWTs : a Factory class , used to create JWT tokens
				.setSubject((userPrincipal.getUsername())) // setting subject part of the token(typically user //
															// name/email)
				.setIssuedAt(new Date())// Sets the JWT Claims iat (issued at) value of current date
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))// Sets the JWT Claims exp
																					// (expiration) value.
				// setting a custom claim
				.claim("userId", userPrincipal.getId())
				.claim("authorities", getAuthoritiesInString(userPrincipal.getAuthorities()))
				.signWith(key, SignatureAlgorithm.HS512) // Signs the constructed JWT using the specified
															// algorithm with the specified key, producing a
															// JWS(Json web signature=signed JWT)

				// Using token signing algo : HMAC using SHA-512
				.compact();// Actually builds the JWT and serializes it to a compact, URL-safe string
	}

	// this method will be invoked by our custom JWT filter
	public String getUserNameFromJwtToken(Claims claims) {
		return claims.getSubject();
	}

	// this method will be invoked by our custom filter
	public Claims validateJwtToken(String jwtToken) {
		// try {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().
		// Sets the signing key used to verify JWT digital signature.
				parseClaimsJws(jwtToken).getBody();// Parses the signed JWT returns the resulting Jws<Claims> instance
		// throws exc in case of failures in verification
		return claims;
	}
	// Accepts Collection<GrantedAuthority> n rets comma separated list of it's
	// string form

	private String getAuthoritiesInString(Collection<? extends GrantedAuthority> authorities) {
		String authorityString = authorities.stream().map(authority -> authority.getAuthority())
				.collect(Collectors.joining(","));
		System.out.println(authorityString);
		return authorityString;
	}

	public List<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
		String authString = (String) claims.get("authorities");
		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authString);
		authorities.forEach(System.out::println);
		return authorities;
	}

	public Date getExpirationDateFromToken(String token) {
		Claims claims = validateJwtToken(token);
		return claims.getExpiration();
	}
}

//Imagine you're building a secure web application where users need to log in. Once a user logs in, the server generates a JWT as proof 
//that the user has been authenticated. This token includes information like the user's identity and roles (permissions), 
//but it doesn’t contain sensitive data like the password.

//Here's how this utility works in the application:

//Generating the Token: After the user logs in, the generateJwtToken method creates a token containing the user's username, user ID, roles, 
//and an expiration date. The token is signed with a secret key so that no one can tamper with it.

//Storing and Using the Token: This token is sent to the user's browser and stored there (usually in local storage or a cookie). 
//For every subsequent request, the token is sent back to the server to prove the user's identity.

//Validating the Token: Every time the server receives a request with a token, the validateJwtToken method 
//checks whether the token is valid, unaltered, and not expired. If the token is valid, the server trusts the request and proceeds.

//Extracting Information: The server can extract the username and roles from the token using methods like getUserNameFromJwtToken and getAuthoritiesFromClaims. 
//This information is used to ensure that the user has the right permissions to access specific resources.
