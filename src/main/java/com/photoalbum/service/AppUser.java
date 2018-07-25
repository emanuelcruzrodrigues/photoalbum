package com.photoalbum.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SuppressWarnings("serial")
public class AppUser implements UserDetails{
	
	private String userName;
	private String password;

	public AppUser(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "USER";
			}});
	}

	@Override
	public String getPassword() {
		return new BCryptPasswordEncoder().encode(password);
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
