package org.example.expert.config;

import lombok.RequiredArgsConstructor;

import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	// Lv2-9 Security Filter
	private final JwtUtil jwtUtil;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
	 return  http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("auth/signin", "auth/signup").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore( jwtFilter(), UsernamePasswordAuthenticationFilter.class)
			.build();

	}

	@Bean
	public JwtFilter jwtFilter(){return new JwtFilter(jwtUtil);}
}
