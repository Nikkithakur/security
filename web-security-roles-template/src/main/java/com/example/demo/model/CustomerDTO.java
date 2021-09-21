package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CustomerDTO {
	private Integer customerId;
	@NotBlank(message = "username can't be null or empty")
	private String username;
	@NotBlank(message = "password can't be null or empty")
	private String password;
	private boolean enabled = true;
	@NotEmpty(message = "authorities can't be null or empty")
	private Set<String> authorities = new HashSet<>();
}
