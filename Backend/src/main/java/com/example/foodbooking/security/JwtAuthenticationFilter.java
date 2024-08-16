package com.example.foodbooking.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.foodbooking.service.RedisService;

import io.jsonwebtoken.Claims;

@Component // spring bean : can be injected in other spring beans
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	// token verification
	// dep : JWT utils
	@Autowired
	private JwtUtils utils;

	@Autowired
	private RedisService redisService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// check auth header from incoming request
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			// => req header contains JWT
			String jwt = authHeader.substring(7);

			if (!redisService.isTokenBlacklisted(jwt)) {
				// validate JWT
				Claims payloadClaims = utils.validateJwtToken(jwt);
				// get user name from the claims
				String email = utils.getUserNameFromJwtToken(payloadClaims);
				// get granted authorities as a custom claim
				List<GrantedAuthority> authorities = utils.getAuthoritiesFromClaims(payloadClaims);
				// add username/email n granted authorities in Authentication object
				// Load user details
				CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
						authorities);
				// save this auth token under spring sec so that subsequent filters will NOT
				// retry the auth again
				SecurityContextHolder.getContext().setAuthentication(token);
				System.out.println("saved auth token in sec ctx");
			}
		}
		filterChain.doFilter(request, response);// to continue with remaining chain of spring sec filters

	}
	//When a user sends a request to the server, this filter checks if the request contains a JWT.
	//If the JWT is present, it first checks whether the token is blacklisted (e.g., because the user logged out).
	//If the token is valid and not blacklisted, the filter extracts the user’s email and roles from the token.
	//It then loads additional user details from the database and creates an authentication object.
	//This authentication object is stored in the security context, making the user recognized as authenticated for the rest of the request.
	//The request is then passed along to the next filter or controller to process the user's request.
	//In essence, this filter is the gatekeeper that verifies the user's identity and permissions before allowing them access to the application’s resources.








//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// check auth header from incoming request
//		String authHeader = request.getHeader("Authorization");
//		if (authHeader != null && authHeader.startsWith("Bearer ")) {
//			// => req header contains JWT
//			String jwt = authHeader.substring(7);
//			// validate JWT
//			Claims payloadClaims = utils.validateJwtToken(jwt);
//			// get user name from the claims
//			String email = utils.getUserNameFromJwtToken(payloadClaims);
//			// get granted authorities as a custom claim
//			List<GrantedAuthority> authorities = utils.getAuthoritiesFromClaims(payloadClaims);
//			// add username/email n granted authorities in Authentication object
//			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, null,
//					authorities);
//			// save this auth token under spring sec so that subsequent filters will NOT
//			// retry the auth again
//			SecurityContextHolder.getContext().setAuthentication(token);
//			System.out.println("saved auth token in sec ctx");
//		}
//		filterChain.doFilter(request, response);// to continue with remaining chain of spring sec filters
//
//	}

}
