package com.example.demo.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Handle exception
 */
@ControllerAdvice
public class CustomExceptionHandler{
 
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
    	Map<String, Object> responseBody = new LinkedHashMap<>();
    	responseBody.put("timestamp", new Date());
        responseBody.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        responseBody.put("error", ex.getMessage());
        
        return new ResponseEntity<Object>(
        		responseBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}