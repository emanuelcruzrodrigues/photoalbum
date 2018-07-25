package com.photoalbum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	@Value("${album.username}")
	private String userName;
	
	@Value("${album.password}")
	private String password;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (!this.userName.toLowerCase().equals(username.toLowerCase())) {
			throw new UsernameNotFoundException("User not found!");
		}
		return new AppUser(this.userName, password);
	}


}
