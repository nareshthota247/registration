package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.AuthorityType;
import com.example.demo.model.ConfirmationToken;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;

@Service
public interface UserService {
	

	Role getRole(AuthorityType type);
	User findByEmailIdIgnoreCase(String email);
	ConfirmationToken findByConfirmationToken(String token);
	ConfirmationToken saveConfirmationToken(User user);
	User saveUser(User user);
	
	

}
