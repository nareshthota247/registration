package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ConfirmationToken;
import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailSenderService;

@RestController
@RequestMapping("/api")
public class UserRegistrationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	@PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Status> registration(@RequestBody User user) {

		User existingUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
		if (existingUser != null) {
			return ResponseEntity.ok(new Status("Failed", "Email already exists!"));
		} else {
			user.setPassword(user.getPassword());
			userRepository.save(user);

			ConfirmationToken confirmationToken = new ConfirmationToken(user);

			confirmationTokenRepository.save(confirmationToken);

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
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		if(token != null)
		{
			User user = userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId());
			user.setEnabled(true);
			userRepository.save(user);
			return ResponseEntity.ok(new Status("Success", "accountVerified"));
		}
		else
		{
			return ResponseEntity.ok(new Status("Failed", "account not Verified"));
		}
		
	}
	
	@RequestMapping(value="/forgot-password", method=RequestMethod.POST)
	public ResponseEntity<Status>  forgotUserPassword(@RequestBody User user) {
		User existingUser = userRepository.findByEmailIdIgnoreCase(user.getEmailId());
		if(existingUser != null) {
			// create token
			ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
			
			// save it
			confirmationTokenRepository.save(confirmationToken);
			
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
	
	@RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Status> validateResetToken(@RequestParam("token")String confirmationToken, @RequestParam("newPassword")String newPassword)
	{
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
		
		if(token != null) {
			User user = userRepository.findByEmailIdIgnoreCase(token.getUser().getEmailId());
			user.setEnabled(true);
			user.setPassword(newPassword);
			userRepository.save(user);
			return ResponseEntity.ok(new Status("Success", "Password Reseted"));
		} else {
			return ResponseEntity.ok(new Status("Failed", "Token broken"));
		}
	}	

}
