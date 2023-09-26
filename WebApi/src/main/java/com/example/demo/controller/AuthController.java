package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.Date;

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

import com.example.demo.dto.SigninRequestDto;
import com.example.demo.dto.SigninResponseDto;
import com.example.demo.dto.SignupRequestDto;
import com.example.demo.dto.SignupResponseDto;
import com.example.demo.dto.ValidateOtpRequestDto;
import com.example.demo.dto.ValidateOtpResponseDto;
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
	public ResponseEntity<SigninResponseDto> authenticateUser(@RequestBody SigninRequestDto signinRequestDto) {
		try {
			SigninResponseDto response = new SigninResponseDto();
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					signinRequestDto.getUsername(), signinRequestDto.getPassword()));
			if (!userRepository.getActive(authentication.getName())) {
				response.setMsg("User is not active!");
				return ResponseEntity.ok().body(response);
			}
			int otp = otpService.generateOTP(authentication.getName());
			response.setOtp(otp);
			response.setMsg("Otp is created!");
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			SigninResponseDto response = new SigninResponseDto();
			response.setMsg("Username or password is incorrect!");
			return ResponseEntity.ok().body(response);
		}
	}

	@PostMapping(value = { "/validateOtp" })
	public ResponseEntity<ValidateOtpResponseDto> validateOtp(
			@RequestBody ValidateOtpRequestDto validateOtpRequestDto) {

		String username = validateOtpRequestDto.getUsername();
		int requestOtp = validateOtpRequestDto.getOtp();
		ValidateOtpResponseDto response = new ValidateOtpResponseDto();
		
		if (requestOtp >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (requestOtp == serverOtp) {
					otpService.clearOTP(username);
				}
				User user = userRepository.findByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
				
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
	public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) throws ParseException {
		SignupResponseDto signupResponseDto = new SignupResponseDto();
		
		if(userRepository.existsByUsername(signupRequestDto.getUsername())) {
			signupResponseDto.setMsg("Username is already taken!");
			return ResponseEntity.ok().body(signupResponseDto);
		}
		
		if(userRepository.existsByEmail(signupRequestDto.getEmail())) {
			signupResponseDto.setMsg("Email is already taken!");
			return ResponseEntity.ok().body(signupResponseDto);
		}
		
		User user = new User();
		UUID uuid = UUID.randomUUID();  
		user.setId(uuid.toString());
		user.setFullName(signupRequestDto.getFullName());
		user.setAvatarUrl(signupRequestDto.getAvatarUrl());
		user.setUsername(signupRequestDto.getUsername());
		user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
		
		Role role = roleRepository.findByRoleName("user").get();
        user.addRole(role);
		userRepository.save(user);
		
		UserInfor userInfor = new UserInfor();
		userInfor.setId(uuid.toString());
		userInfor.setIsActive(1);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date parsed = format.parse(signupRequestDto.getDateOfBirth());
		userInfor.setDateOfBirth(new java.sql.Date(parsed.getTime()));
		
		userInforRepository.save(userInfor);
		signupResponseDto.setMsg("Registration successful");
		return ResponseEntity.ok().body(signupResponseDto);
		
	}
}
