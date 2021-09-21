package com.example.demo.controller;

import static com.example.demo.utils.ServiceConstants.*;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.security.UserDetailsServiceImpl;
import com.example.demo.model.AuthRequestDTO;
import com.example.demo.model.AuthResponseDTO;
import com.example.demo.model.AuthorityDTO;
import com.example.demo.model.CustomerDTO;
import com.example.demo.service.WebSecurityTemplateServiceImpl;
import com.example.demo.utils.JWTTokenUtil;

@Validated
@RestController
public class WebSecurityTemplateController {

	@Autowired
	WebSecurityTemplateServiceImpl serviceImpl;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	UserDetailsServiceImpl customerImpl;

	@GetMapping("/home")
	public String welcomeToHomePage() {
		return "Hello! welcome to homepage";
	}

	@PostMapping("/generatetoken")
	public AuthResponseDTO generateToken(@RequestBody AuthRequestDTO authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		} catch (Exception ex) {
			throw new Exception("inavalid username/password");
		}
		final UserDetails userDetails = customerImpl.loadUserByUsername(authRequest.getUserName());
		return new AuthResponseDTO(jwtTokenUtil.generateToken(userDetails));
	}

	@RolesAllowed(value = { ADMIN, USER, GUEST })
	@GetMapping("/secured")
	public String welcomeToSecuredPage(Principal principal) {
		return principal.toString();
	}

	@RolesAllowed(value = { ADMIN, USER })
	@PostMapping("/createcustomer")
	public ResponseEntity<List<CustomerDTO>> createcust(
			@Valid @NotEmpty(message = "customersList can't be empty") @RequestBody List<CustomerDTO> customersList)
			throws RoleNotFoundException {
		return ResponseEntity.ok(serviceImpl.createCustomer(customersList));
	}

	@RolesAllowed(value = { ADMIN })
	@PostMapping("/createrole")
	public ResponseEntity<List<AuthorityDTO>> createrole(
			@Valid @NotEmpty(message = "authorities can't be empty") @RequestBody List<AuthorityDTO> authorities) {
		return ResponseEntity.ok(serviceImpl.createAuthority(authorities));
	}

	@RolesAllowed(value = { ADMIN })
	@DeleteMapping("/customer/delete/{id}")
	public String delcust(@PathVariable Integer id) {
		return serviceImpl.deleteCustomer(id);
	}

	@RolesAllowed(value = { ADMIN,})
	@DeleteMapping("role/delete/{id}")
	public String createrole(@PathVariable Integer id) {
		return serviceImpl.deleteAuthority(id);
	}
}
