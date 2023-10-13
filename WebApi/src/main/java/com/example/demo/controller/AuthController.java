package com.example.demo.controller;

import java.text.ParseException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.request.ForgotPasswordReqDto;
import com.example.demo.dto.request.SigninReqDto;
import com.example.demo.dto.request.SignupReqDto;
import com.example.demo.dto.request.ValidateOtpReqDto;
import com.example.demo.dto.response.SigninResDto;
import com.example.demo.entity.User;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.AuthService;
import com.example.demo.service.OTPService;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "API thực hiện xác thực user, đăng ký mới user")
@RestController
@RequestMapping("/api/auth")
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
	public ResponseEntity<SigninResDto> authenticateUser(
			@Valid @RequestBody(required = true) SigninReqDto request) {
		
		SigninResDto response = new SigninResDto();
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			if (!authService.isActive(request.getUsername())) {
				response.setMsg("User is not active!");
				return ResponseEntity.ok().body(response);
			}
			int otp = otpService.generateOTP(authentication.getName());
			response.setOtp(otp);
			response.setMsg("Otp is created!");
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response.setMsg("Username or password is incorrect!");
			return ResponseEntity.ok().body(response);
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
		}
		
		return ResponseEntity.ok().body(prefixAuthen + jwtToken);
	}

	@Operation(summary = "Register new user")
	@PostMapping(value = { "/signup" }, consumes = "multipart/form-data")
	public ResponseEntity<?> signup(
			@RequestPart(value = "file", required = false) @NotNull MultipartFile avatarFile, 
			@Valid @RequestPart(value = "request", required = true) SignupReqDto request) throws ParseException {
		
		if (userService.getByUsername(request.getUsername()) != null) {
			return ResponseEntity.ok().body("Username is already taken!");
		}
		
		if (userService.saveUser(request, avatarFile) != null) {
			return ResponseEntity.ok().body("Registration successful");
		} else {
			return ResponseEntity.internalServerError().body("Registration error");
		}

	}

	@Operation(summary = "Forgot password and get token")
	@PostMapping(value = { "/forgot-password" })
	public ResponseEntity<?> forgotPassword(
			@Valid @RequestBody(required = true) ForgotPasswordReqDto request) {

		User user = userService.getByUsername(request.getUsername());
		if (user == null) {
			return ResponseEntity.ok().body("Username is valid!");
		}

		// Create jwt token
		String jwtToken = jwtUtils.generateJwtToken(request.getUsername());

		return ResponseEntity.ok().body(prefixAuthen + jwtToken);
	}

}
