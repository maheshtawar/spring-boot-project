package com.example.spring_boot_project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.employeedirectorysystem.filter.JwtAuthenticationFilter;
import com.example.employeedirectorysystem.service.CustomUserDetailsService;

/**
 * Web security configuration class.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	private static final String[] AUTH_WHITELIST = { "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
			"/swagger-resources/**", "/webjars/**", "/configuration/ui", "/configuration/security", "/auth/**",
			"/public/**" // Added new endpoint
	};

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll()
//						.requestMatchers("/employees/**").hasAnyRole("ADMIN", "HR") // Use hasRole for "ROLE_ADMIN"
						.anyRequest().authenticated()) // Secure all other endpoints
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
}
