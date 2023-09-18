package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig{
	
//	@Autowired
//	private UserDetailsService userDetailService;
	
//    public SpringSecurityConfig(UserDetailsService userDetailsService){
//        this.userDetailService = userDetailsService;
//    }
	
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
    @Bean
    public AuthenticationManager authenticationManager(
                                 AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
        	.authorizeRequests()
        		.antMatchers("/api/auth/**").permitAll()
        		.antMatchers("/swagger-ui.html#/**").permitAll()
        		.antMatchers("/webjars/**").permitAll()
        		.antMatchers("/api/demo/**").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}
