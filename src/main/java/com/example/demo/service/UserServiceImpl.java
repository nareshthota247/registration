package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.AuthorityType;
import com.example.demo.model.ConfirmationToken;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Override
	public Role getRole(AuthorityType type) {
		return roleRepository.findByName(type);
	}

	@Override
	public User findByEmailIdIgnoreCase(String email) {
		return userRepository.findByEmailIdIgnoreCase(email);
	}

	@Override
	public ConfirmationToken findByConfirmationToken(String token) {
		return confirmationTokenRepository.findByConfirmationToken(token);
	}

	@Override
	public ConfirmationToken saveConfirmationToken(User user) {
		return confirmationTokenRepository.save(new ConfirmationToken(user));
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

}
