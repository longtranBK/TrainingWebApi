package com.example.demo.controller;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.ForgotPasswordReqDto;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

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

	@PostMapping(value = { "/signin" })
	public ResponseEntity<SigninResDto> authenticateUser(@Valid @RequestBody SigninReqDto request) {
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

	@PostMapping(value = { "/validate-otp" })
	public ResponseEntity<ValidateOtpResDto> validateOtp(@Valid @RequestBody ValidateOtpReqDto request) {

		String username = request.getUsername();
		int requestOtp = request.getOtp();
		int serverOtp = otpService.getOtp(username);

		ValidateOtpResDto response = new ValidateOtpResDto();
		response.setToken(null);

		if (requestOtp == serverOtp) {
			otpService.clearOTP(username);
			// Create jwt token
			String jwtToken = jwtUtils.generateJwtToken(username);
			response.setToken(jwtToken);
		}
		
		return ResponseEntity.ok().body(response);
	}

	@PostMapping(value = { "/signup" })
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto request) throws ParseException {
		if (userService.getByUsername(request.getUsername()) != null) {
			return ResponseEntity.ok().body("Username is already taken!");
		}
		userService.saveUser(request);

		return ResponseEntity.ok().body("Registration successful");
	}

	@PostMapping(value = { "/forgot-password" })
	@Transactional(rollbackOn = { Exception.class, Throwable.class })
	public ResponseEntity<ForgotPasswordResDto> forgotPassword(@Valid @RequestBody ForgotPasswordReqDto requestDto,
			HttpServletRequest request) {
		ForgotPasswordResDto response = new ForgotPasswordResDto();
		User user = userService.getByUsername(requestDto.getUsername());
		if (user == null) {
			response.setMsg("Username is valid!");
			return ResponseEntity.ok().body(response);
		}

		// Create jwt token
		String jwtToken = jwtUtils.generateJwtToken(requestDto.getUsername());
		String resetPasswordLink = request.getRequestURL().toString() + "/reset_password?token=" + jwtToken;

		response.setLinkResetPassword(resetPasswordLink);
		response.setMsg("We have sent a reset password link to your email. Please check.");

		return ResponseEntity.ok().body(response);
	}

}
