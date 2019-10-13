package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/secure")
public class UserMaintanceRepository {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/getUserInfo")
	public ResponseEntity<User> getUserInfo(@RequestParam String email) {
		
		return ResponseEntity.ok(userRepository.findByEmailIdIgnoreCase(email));
		
	}

}
