package com.example.demo.exception;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<HashMap<String, String>> fieldList = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			HashMap<String, String> map = new HashMap<>();
			String field = ((FieldError) error).getField();
			String defaultMessage = error.getDefaultMessage();
			map.put("fieldname", field);
			map.put("errorMessage", defaultMessage);
			map.put("timestamp", Timestamp.from(Instant.now()).toString());
			fieldList.add(map);
		});
		return ResponseEntity.badRequest().body(fieldList);   
	}
	
	@ExceptionHandler(value = ConstraintViolationException.class)
	protected ResponseEntity<Object> constraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {
		List<HashMap<String, String>> fieldList = new ArrayList<>();
		ex.getConstraintViolations().forEach(error->{
			HashMap<String, String> map = new HashMap<>();
			String field = error.getPropertyPath().toString();
			String defaultMessage = error.getMessage();
			map.put("fieldname", field);
			map.put("errorMessage", defaultMessage);
			map.put("timestamp", Timestamp.from(Instant.now()).toString());
			fieldList.add(map);
		});
		return ResponseEntity.badRequest().body(fieldList); 
	}
	
	

}
