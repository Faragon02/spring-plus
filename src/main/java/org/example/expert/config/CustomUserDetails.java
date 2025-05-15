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
	private final Claims claims;

	public CustomUserDetails(Claims claims){
		this.claims =claims;
	}
	public Long getId(){
		return Long.parseLong(claims.getSubject());
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("userRole").toString()));
	}

	@Override
	public String getPassword() {
		return  claims.get("password").toString();
	}

	@Override
	public String getUsername() {
		return claims.get("email").toString();
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
