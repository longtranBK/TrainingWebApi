package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.dto.request.ForgotPasswordReqDto;
import com.example.demo.dto.request.SigninReqDto;
import com.example.demo.dto.request.ValidateOtpReqDto;
import com.example.demo.dto.response.SigninResDto;
import com.example.demo.entity.User;
import com.example.demo.repository.AvatarImageRepository;
import com.example.demo.repository.CommentLikeRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostImageRepository;
import com.example.demo.repository.PostLikeRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserFriendRepository;
import com.example.demo.repository.UserInforRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.AuthService;
import com.example.demo.service.OTPService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class )
@EntityScan("com.example.demo.entity")
@ComponentScan({"com.example.demo"})
@EnableJpaRepositories(basePackages="com.example.demo.repository")
@MockBeans({ 
	@MockBean(AvatarImageRepository.class),
	@MockBean(CommentLikeRepository.class),
	@MockBean(CommentRepository.class),
	@MockBean(PostImageRepository.class),
	@MockBean(PostLikeRepository.class),
	@MockBean(PostRepository.class),
	@MockBean(ReportRepository.class),
	@MockBean(RoleRepository.class),
	@MockBean(UserFriendRepository.class),
	@MockBean(UserInforRepository.class),
	@MockBean(UserRepository.class)
})
public class AuthControllerTests {

	private String uriApiSignin = "/v1/auth/signin";

	private String uriApiValidateOtp = "/api/auth/validate-otp";

	private String uriApiSignup = "/api/auth/signup";

	private String uriApiForgotPwd = "/api/auth/forgot-password";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthenticationManager authenticationManager;

	Authentication authentication = Mockito.mock(Authentication.class);

	@MockBean
	AuthService authService;

	@MockBean
	OTPService otpService;

	@MockBean
	private JwtUtils jwtUtils;

	@MockBean
	private UserService userService;

	@Test
	void authenticateUser_authenOk_isActive() throws Exception {
		SigninReqDto request = new SigninReqDto();
		request.setUsername("longth");
		request.setPassword("Longth@12345");

		UsernamePasswordAuthenticationToken userTest = new UsernamePasswordAuthenticationToken(request.getUsername(),
				request.getPassword());
		when(authenticationManager.authenticate(userTest)).thenReturn(authentication);
		Mockito.when(authentication.getName()).thenReturn("longth@gmail.com");

		when(authService.isActive(request.getUsername())).thenReturn(true);
		when(otpService.generateOTP(request.getUsername())).thenReturn(111111);

		MvcResult mvcResult = mockMvc.perform(post(uriApiSignin)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

	}

	@Test
	void authenticateUser_authenOk_isNotActive() throws Exception {
		SigninReqDto request = new SigninReqDto();
		request.setUsername("longth@gmail.com");
		request.setPassword("longth");

		UsernamePasswordAuthenticationToken userTest = new UsernamePasswordAuthenticationToken(request.getUsername(),
				request.getPassword());
		when(authenticationManager.authenticate(userTest)).thenReturn(authentication);
		Mockito.when(authentication.getName()).thenReturn("longth@gmail.com");

		when(authService.isActive(request.getUsername())).thenReturn(false);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uriApiSignin)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		SigninResDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				SigninResDto.class);
		assertEquals("User is not active!", response.getMsg());

	}

	@Test
	void authenticateUser_authenNg() throws Exception {
		SigninReqDto request = new SigninReqDto();
		request.setUsername("longth@gmail.com");
		request.setPassword("longth");

		UsernamePasswordAuthenticationToken userTest = new UsernamePasswordAuthenticationToken(request.getUsername(),
				request.getPassword());
		when(authenticationManager.authenticate(userTest)).thenThrow(BadCredentialsException.class);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uriApiSignin)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		SigninResDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				SigninResDto.class);
		assertEquals("Username or password is incorrect!", response.getMsg());
	}

	@Test
	void validateOtp_otpExactly() throws Exception {
		ValidateOtpReqDto request = new ValidateOtpReqDto();
		request.setOtp(111111);
		request.setUsername("longth@gmail.com");
		when(otpService.getOtp(request.getUsername())).thenReturn(111111);
		doNothing().when(otpService).clearOTP(request.getUsername());
		when(jwtUtils.generateJwtToken(request.getUsername())).thenReturn("jwtToken");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uriApiValidateOtp)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String token = mvcResult.getResponse().getContentAsString();
		assertEquals("Bearer jwtToken", token);
	}

	@Test
	void validateOtp_otpNotExactly() throws Exception {
		ValidateOtpReqDto request = new ValidateOtpReqDto();
		request.setOtp(111111);
		request.setUsername("longth@gmail.com");
		when(otpService.getOtp(request.getUsername())).thenReturn(222222);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uriApiValidateOtp)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String token = mvcResult.getResponse().getContentAsString();
		assertEquals("Bearer ", token);
	}

	@Test
	void signup_usernameNotExists_saveOk() throws Exception {
//		SignupReqDto req = new SignupReqDto();
//		req.setFullName("Trần Hoàng Long");
//		req.setUsername("longth@gmail.com");
//		req.setPassword("longth");
//		req.setSex(1);
//		req.setDateOfBirth("1993-02-09");
//
//		MockMultipartFile avatarFile = new MockMultipartFile("file", "", "image/png", "Avatar of user".getBytes());
//		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
//				objectMapper.writeValueAsString(req).getBytes());
//
//		when(userService.getByUsername(req.getUsername())).thenReturn(null);
//		when(userService.saveUser(req, avatarFile)).thenReturn(new User());
//
//		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uriApiSignup).file(avatarFile)
//				.file(request).contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
//		int status = mvcResult.getResponse().getStatus();
//		assertEquals(200, status);
//
//		String token = mvcResult.getResponse().getContentAsString();
//		assertEquals("Registration successful", token);
	}
	
	@Test
	void signup_usernameNotExists_saveNg() throws Exception {
//		SignupReqDto req = new SignupReqDto();
//		req.setFullName("Trần Hoàng Long");
//		req.setUsername("longth@gmail.com");
//		req.setPassword("longth");
//		req.setSex(1);
//		req.setDateOfBirth("1993-02-09");
//
//		MockMultipartFile avatarFile = new MockMultipartFile("file", "", "image/png", "Avatar of user".getBytes());
//		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
//				objectMapper.writeValueAsString(req).getBytes());
//
//		when(userService.getByUsername(req.getUsername())).thenReturn(null);
//		when(userService.saveUser(req, avatarFile)).thenReturn(null);
//
//		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uriApiSignup).file(avatarFile)
//				.file(request).contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		assertEquals(500, status);
	}

	@Test
	void signup_usernameExists() throws Exception {
//		SignupReqDto req = new SignupReqDto();
//		req.setFullName("Trần Hoàng Long");
//		req.setUsername("longth@gmail.com");
//		req.setPassword("longth");
//		req.setSex(1);
//		req.setDateOfBirth("1993-02-09");
//
//		MockMultipartFile avatarFile = new MockMultipartFile("file", "", "image/png", "Avatar of user".getBytes());
//		MockMultipartFile request = new MockMultipartFile("request", "", "application/json",
//				objectMapper.writeValueAsString(req).getBytes());
//
//		when(userService.getByUsername(req.getUsername())).thenReturn(new User());
//
//		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart(uriApiSignup).file(avatarFile)
//				.file(request).contentType(MediaType.MULTIPART_FORM_DATA).content("")).andReturn();
//
//		int status = mvcResult.getResponse().getStatus();
//		assertEquals(200, status);
//
//		String msg = mvcResult.getResponse().getContentAsString();
//		assertEquals("Username is already taken!", msg);
	}

	@Test
	void forgotPassword_UsernameNotExist() throws Exception {
		ForgotPasswordReqDto request = new ForgotPasswordReqDto();
		request.setUsername("longth@gmail.com");

		when(userService.findByUsername(request.getUsername())).thenReturn(null);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uriApiForgotPwd)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String msg = mvcResult.getResponse().getContentAsString();
		assertEquals("Username is valid!", msg);
	}

	@Test
	void forgotPassword_UsernameExist() throws Exception {
		ForgotPasswordReqDto request = new ForgotPasswordReqDto();
		request.setUsername("longth@gmail.com");

		when(userService.findByUsername(request.getUsername())).thenReturn(new User());
		when(jwtUtils.generateJwtToken(request.getUsername())).thenReturn("jwtToken");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uriApiForgotPwd)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String token = mvcResult.getResponse().getContentAsString();
		assertEquals("Bearer jwtToken", token);
	}
}
