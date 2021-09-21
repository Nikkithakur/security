package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
	String accessToken;
	String refreshToken;

	public AuthResponseDTO(String accessToken) {
		this.accessToken = accessToken;
	}
}
