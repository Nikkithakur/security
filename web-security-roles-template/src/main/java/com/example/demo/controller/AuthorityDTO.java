package com.example.demo.controller;




import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
	@NotBlank (message = "authority can't be null or empty")
	private String authority;
}
