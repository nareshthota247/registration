package com.example.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AuthorityType;
import com.example.demo.model.ConfirmationToken;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.model.UserDTO;
import com.example.demo.service.EmailSenderService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRegistrationController {

	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	UserService userServiceImpl;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	ModelMapper modelMapper;

	@PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Status> registration(@RequestBody UserDTO userDto) {

		User existingUser = userServiceImpl.findByEmailIdIgnoreCase(userDto.getEmailId());
		if (existingUser != null) {
			return ResponseEntity.ok(new Status("Failed", "Email already exists!"));
		} else {
			
			User user = modelMapper.map(userDto, User.class);
			user.setPassword(encoder.encode(user.getPassword()));
			user.setRole(userServiceImpl.getRole(AuthorityType.ROLE_USER));
			userServiceImpl.saveUser(user);


			ConfirmationToken confirmationToken = userServiceImpl.saveConfirmationToken(user);

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEmailId());
			mailMessage.setSubject("Complete Registration!");
			mailMessage.setText("To confirm your account, please click here : "
					+ "http://localhost:8082/api/confirm-account?token=" + confirmationToken.getConfirmationToken());

			emailSenderService.sendEmail(mailMessage);

		}

		return ResponseEntity.ok(new Status("Success", "Registed Successfully!"));

	}
	
	@RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Status> confirmUserAccount(@RequestParam("token")String confirmationToken) {
		ConfirmationToken token = userServiceImpl.findByConfirmationToken(confirmationToken);
		
		if(token != null)
		{
			User user = userServiceImpl.findByEmailIdIgnoreCase(token.getUser().getEmailId());
			user.setEnabled(true);
			userServiceImpl.saveUser(user);
			return ResponseEntity.ok(new Status("Success", "accountVerified"));
		}
		else
		{
			return ResponseEntity.ok(new Status("Failed", "account not Verified"));
		}
		
	}
	
	@PostMapping(value="/forgot-password")
	public ResponseEntity<Status>  forgotUserPassword(@RequestBody UserDTO userDto) {
		User existingUser = userServiceImpl.findByEmailIdIgnoreCase(userDto.getEmailId());
		if(existingUser != null) {
			// create token
			ConfirmationToken confirmationToken = userServiceImpl.saveConfirmationToken(existingUser);
			
			// create the email
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(existingUser.getEmailId());
			mailMessage.setSubject("Complete Password Reset!");
			mailMessage.setText("To complete the password reset process, please click here: "
			+"http://localhost:8082/confirm-reset?token="+confirmationToken.getConfirmationToken());
			
			emailSenderService.sendEmail(mailMessage);

			return ResponseEntity.ok(new Status("Success", "Request to reset password received. Check your inbox for the reset link."));

		} else {	
			return ResponseEntity.ok(new Status("Success", "This email does not exist!"));
		}
	}
	
	@PostMapping(value="/confirm-reset")
	public ResponseEntity<Status> validateResetToken(@RequestParam("token")String confirmationToken, @RequestParam("newPassword")String newPassword)
	{
		ConfirmationToken token = userServiceImpl.findByConfirmationToken(confirmationToken);
		
		if(token != null) {
			User user = userServiceImpl.findByEmailIdIgnoreCase(token.getUser().getEmailId());
			user.setEnabled(true);
			user.setPassword(newPassword);
			userServiceImpl.saveUser(user);
			return ResponseEntity.ok(new Status("Success", "Password Reseted"));
		} else {
			return ResponseEntity.ok(new Status("Failed", "Token broken"));
		}
	}	

}
