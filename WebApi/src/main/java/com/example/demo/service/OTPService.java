package com.example.demo.service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OTPService {
	
	private static final Integer EXPIRE_MINS = 5;
	private LoadingCache<String, Integer> otpCache;
	
	/**
	 * 
	 */
	public OTPService() {
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>(){
			public Integer load(String key) {
				return 0;
			}
		});
		
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public int generateOTP(String key) {
		Random random = new Random();
		int otp = 100000 + random.nextInt(100000);
		otpCache.put(key, otp);
		return otp;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getOtp(String key) {
		try {
			return otpCache.get(key);
		}catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 
	 * @param key
	 */
	public void clearOTP(String key) {
		otpCache.invalidate(key);
	}
}
