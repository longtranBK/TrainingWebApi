package com.example.demo.controller;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.*;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfor;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.OTPService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private OTPService otpService;

	@Autowired
	JwtUtils jwtUtils;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserInforRepository userInforRepository;

	@PostMapping(value = { "/signin" })
	public ResponseEntity<SigninResDto> authenticateUser(@Valid @RequestBody SigninReqDto request) {
		try {
			SigninResDto response = new SigninResDto();
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					request.getUsername(), request.getPassword()));
			if (!userRepository.getActive(authentication.getName())) {
				response.setMsg("User is not active!");
				return ResponseEntity.ok().body(response);
			}
			int otp = otpService.generateOTP(authentication.getName());
			response.setOtp(otp);
			response.setMsg("Otp is created!");
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			SigninResDto response = new SigninResDto();
			response.setMsg("Username or password is incorrect!");
			return ResponseEntity.ok().body(response);
		}
	}

	@PostMapping(value = { "/validateOtp" })
	public ResponseEntity<ValidateOtpResDto> validateOtp(@Valid
			@RequestBody ValidateOtpReqDto request) {

		String username = request.getUsername();
		int requestOtp = request.getOtp();
		ValidateOtpResDto response = new ValidateOtpResDto();
		
		if (requestOtp >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (requestOtp == serverOtp) {
					otpService.clearOTP(username);
				}
				User user = userRepository.findByUsername(username);
				
				if (user == null) {
					throw new UsernameNotFoundException("User not found with username: " + username);
				}
				
				List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
						.map((role) -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
				
				Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
						user.getPassword(), authorities);
				
				//Create jwt token
				String jwtToken = jwtUtils.generateJwtToken(authentication);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				// Return token
				response.setToken(jwtToken);
				return ResponseEntity.ok().body(response);
			} else {
				response.setToken(null);
				return ResponseEntity.ok().body(response);
			}
		} else {
			response.setToken(null);
			return ResponseEntity.ok().body(response);
		}
	}
	
	@PostMapping(value = { "/signup" })
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public ResponseEntity<SignupResDto> signup(@Valid @RequestBody SignupReqDto request) throws ParseException {
		SignupResDto response = new SignupResDto();
		
		if(userRepository.existsByUsername(request.getUsername())) {
			response.setMsg("Username is already taken!");
			return ResponseEntity.ok().body(response);
		}
		
		if(userRepository.existsByEmail(request.getEmail())) {
			response.setMsg("Email is already taken!");
			return ResponseEntity.ok().body(response);
		}
		
		String uuid = UUID.randomUUID().toString();  
		User user = new User();

		user.setUserId(uuid);
		user.setFullName(request.getFullName());
		user.setAvatarUrl(request.getAvatarUrl());
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		Role role = roleRepository.findByRoleName("user").get();
        user.addRole(role);
		userRepository.save(user);
		
		UserInfor userInfor = new UserInfor();
		userInfor.setUserId(uuid);
		userInfor.setIsActive(1);
		userInfor.setSex(request.getSex());
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date parsed = format.parse(request.getDateOfBirth());
		userInfor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		
		userInforRepository.save(userInfor);
		response.setMsg("Registration successful");
		return ResponseEntity.ok().body(response);
		
	}
	
	@PostMapping(value = { "/forgotPassword" })
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public ResponseEntity<ForgotPasswordResDto> forgotPassword(@Valid @RequestBody ForgotPasswordReqDto forgotPasswordRequestDto, 
			HttpServletRequest request) {
		ForgotPasswordResDto response = new ForgotPasswordResDto();
		User user = userRepository.findByUsernameAndEmail(forgotPasswordRequestDto.getUsername(), forgotPasswordRequestDto.getEmail());
		if(user == null) {
			response.setMsg("Username or email is valid!");
			return ResponseEntity.ok().body(response);
		}
		
		String resetTokenPassword = RandomStringUtils.randomAlphabetic(30);
		user.setResetPasswordToken(resetTokenPassword);
		userRepository.save(user);
		
		String resetPasswordLink = request.getRequestURL().toString() + "/reset_password?token=" + resetTokenPassword;
		response.setLinkResetPassword(resetPasswordLink);
		response.setMsg("We have sent a reset password link to your email. Please check.");
		
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping(value = { "/processResetPassword" })
	@Transactional(rollbackOn = {Exception.class, Throwable.class})
	public ResponseEntity<ProcessResetPasswordResDto> processResetPassword(@Valid @RequestBody ProcessResetPasswordReqDto 
			request) {
		ProcessResetPasswordResDto response = new ProcessResetPasswordResDto();
		User user = userRepository.findByUsernameAndEmailAndResetPasswordToken(
				request.getUsername(), request.getEmail(), 
				request.getResetPasswordToken());
		if(user == null) {
			response.setMsg("Username or email or token is valid!");
			return ResponseEntity.ok().body(response);
		}
		
		user.setResetPasswordToken(null);
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		response.setMsg("Password update succcessful!");
		return ResponseEntity.ok().body(response);
	}
	
}
