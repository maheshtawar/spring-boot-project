package com.example.spring_boot_project.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.employeedirectorysystem.utility.JwtTokenUtility;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenUtility jwtTokenUtility;
	private final UserDetailsService userDetailsService;

	@Autowired
	public JwtAuthenticationFilter(JwtTokenUtility jwtTokenUtility,
			@Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
		this.jwtTokenUtility = jwtTokenUtility;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String token = getTokenFromRequest(request);

			if (token != null && jwtTokenUtility.validateToken(token, getEmailFromToken(token))) {
				String email = jwtTokenUtility.extractEmail(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);

				// Create an Authentication object with the user details and authorities
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				logger.debug("Authorities: " + userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("JWT token is expired");
		}
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

	private String getEmailFromToken(String token) {
		return jwtTokenUtility.extractEmail(token);
	}
}
