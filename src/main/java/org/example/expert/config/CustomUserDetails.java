package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsMutator;

import java.util.Collection;
import java.util.List;

import org.example.expert.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	private final Long id;
	private final String userRole;
	private final String username;

	public CustomUserDetails(Claims claims){
		this.id = Long.parseLong(claims.getSubject());
		this.userRole =claims.get("userRole").toString();
		this.username = claims.get("email").toString();

	}
	public Long getId(){
		return id;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + userRole ));
	}

	@Override
	public String getPassword() {
		return  null;
	}

	@Override
	public String getUsername() {
		return  username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}
