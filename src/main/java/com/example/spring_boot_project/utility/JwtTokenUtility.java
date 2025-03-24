/**
 * 
 */
package com.example.spring_boot_project.utility;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * @author MaheshT
 *
 */
@Component
public class JwtTokenUtility {

	@Value("${jwt.secret}")
	private String secretKey;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

	public String generateToken(String email, String role) {
		// Prefix the role with "ROLE_"
		String roleWithPrefix = "ROLE_" + role.toUpperCase(); // e.g., "ROLE_ADMIN"

		List<String> roles = Collections.singletonList(roleWithPrefix); // Use the prefixed role

		return Jwts.builder().setSubject(email).claim("authorities", roles) // Store the role as a list
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public boolean validateToken(String token, String userEmail) {
		try {
			final String extractedEmail = extractEmail(token);
			return (userEmail.equals(extractedEmail) && !isTokenExpired(token));
		} catch (Exception e) {
			logger.warn("JWT validation failed: " + e.getMessage());
			return false;
		}
	}

	private boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();

			String username = null;

			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else if (principal instanceof String) {
				username = (String) principal; // In case principal is just a raw username
			}

			// Extract the part before '@' in the email
			if (username != null && username.contains("@")) {
				return username.split("@")[0];
			}

			return username; // If no '@' is found, return as is
		}

		return null; // No authenticated user
	}
}