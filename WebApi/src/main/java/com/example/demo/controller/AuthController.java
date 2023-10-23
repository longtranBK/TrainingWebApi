package com.example.demo.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.ForgotPasswordReqDto;
import com.example.demo.dto.request.ResetPasswordReqDto;
import com.example.demo.dto.request.SigninReqDto;
import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.ValidateOtpReqDto;
import com.example.demo.dto.response.ForgotPasswordResDto;
import com.example.demo.dto.response.SigninResDto;
import com.example.demo.dto.response.ValidateOtpResDto;
import com.example.demo.entity.User;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.AuthService;
import com.example.demo.service.OTPService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API thực hiện xác thực user, đăng ký mới user")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

	private String prefixAuthen = "Bearer ";
	
	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private OTPService otpService;

	@Autowired
	private JwtUtils jwtUtils;

	@Operation(summary = "Sign in with username and password")
	@PostMapping(value = { "/signin" })
	public ResponseEntity<?> authenticateUser(
			@Valid @RequestBody(required = true) SigninReqDto request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			if (!authService.isActive(request.getUsername())) {
				return ResponseEntity.ok().body("User is not active!");
			}
			SigninResDto response = new SigninResDto();
			int otp = otpService.generateOTP(authentication.getName());
			response.setOtp(otp);
			response.setMsg("Otp is created!");
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			return ResponseEntity.ok().body("Username or password is incorrect!");
		}
	}

	@Operation(summary = "Check otp after run api signin")
	@PostMapping(value = { "/validate-otp" })
	public ResponseEntity<?> validateOtp(
			@Valid @RequestBody(required = true) ValidateOtpReqDto request) {
		
		String username = request.getUsername();
		int requestOtp = request.getOtp();
		int serverOtp = otpService.getOtp(username);
		String jwtToken = "";
		
		if (requestOtp == serverOtp) {
			otpService.clearOTP(username);
			// Create jwt token
			jwtToken = jwtUtils.generateJwtToken(username);
			
			ValidateOtpResDto response = new ValidateOtpResDto();
			response.setUserId(userService.findByUsername(username).getUserId());
			response.setToken(prefixAuthen + jwtToken);
			response.setMsg("Token is created!");
			return ResponseEntity.ok().body(response);
		} else {
			return ResponseEntity.ok().body("Username or otp is valid!");
		}
	}

	@Operation(summary = "Register new user")
	@PostMapping(value = { "/signup" })
	public ResponseEntity<?> signup(
			@Valid @RequestBody(required = true) SignupReqDto request) throws ParseException {
		
		if (userService.findByUsernameOrEmail(request.getUsername(), request.getEmail()) != null) {
			return ResponseEntity.ok().body("Username or email is already taken!");
		}
		
		if (userService.saveUser(request) != null) {
			return ResponseEntity.ok().body("Registration successful");
		} else {
			return ResponseEntity.internalServerError().body("Registration error");
		}
	}

	@Operation(summary = "Forgot password and get token")
	@PostMapping(value = { "/forgot-password" })
	public ResponseEntity<?> forgotPassword(
			@Valid @RequestBody(required = true) ForgotPasswordReqDto request) {

		User user = userService.findByUsername(request.getUsername());
		if (user == null) {
			return ResponseEntity.ok().body("User not found!");
		}
		ForgotPasswordResDto response = new ForgotPasswordResDto();
		response.setToken(RandomStringUtils.randomAlphabetic(30));
		response.setMsg("Token forgot password had created!");

		return ResponseEntity.ok().body(response);
	}
	
	@Operation(summary = "Reset and set new password")
	@PutMapping(value = { "/renew-password" })
	public ResponseEntity<?> resetPassword(
			@Valid @RequestBody(required = true) ResetPasswordReqDto request) {
		
		User user = userService.findByUsernameAndToken(request.getUsername(), request.getToken());
		
		if(user == null) {
			return ResponseEntity.ok().body("User not found!");
		}
		
		if (userService.setNewPws(user, request.getNewPassword()) != null) {
			return ResponseEntity.ok().body("Password update succcessful!");
		} else {
			return ResponseEntity.internalServerError().body("Password update error!");
		}
	}

}
