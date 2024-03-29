package com.hcmutap.elearning.service.impl;

import com.hcmutap.elearning.model.UserModel;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {
	@Resource
	private UserService userService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserModel> userModels = userService.findByUsername(username);
		if( !userModels.isEmpty() ) {
			UserModel usr = userModels.getFirst();
			List<GrantedAuthority> authorities = new ArrayList<>();
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(usr.getRole());
			authorities.add(grantedAuthority);
			UserDetails userDetails =  new org.springframework.security.core.userdetails.User(usr.getUsername(), usr.getPassword(), authorities);
			return userDetails;
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}
}
