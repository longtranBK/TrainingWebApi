package com.example.demo.security.jwt;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expirationMs}")
	private int jwtExpirationMs;

    /**
     * Create token JWT 
     * 
     * @param username
     * @return token
     */
	public String generateJwtToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Decode jwt secret
	 * 
	 * @return
	 */
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	/**
	 * Get username from jwt token
	 * 
	 * @param token
	 * @return
	 */
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Check jwt token
	 * 
	 * @param authToken
	 * @return true if parse ok
	 */
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
			return true;
		} catch (Exception e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		}

		return false;
	}
	
}
