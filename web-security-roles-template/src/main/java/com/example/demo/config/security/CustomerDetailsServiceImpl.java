package com.example.demo.config.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.example.demo.entity.CustomerEntity;
import com.example.demo.repo.ICustomerRepo;

@Component
@Transactional
public class CustomerDetailsServiceImpl implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(CustomerDetailsServiceImpl.class);
	
	@Autowired
	ICustomerRepo customerRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomerEntity customerModel = customerRepo.findByusername(username)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User: %s, not found", username)));
		return transformData(customerModel);
	}

	private UserDetails transformData(CustomerEntity customer) {
		User user = new User(customer.getUsername(), customer.getPassword(), getauthority(customer));
		return user;
	}
	
	private List<SimpleGrantedAuthority> getauthority(CustomerEntity customer) {
		try {
		if(!CollectionUtils.isEmpty(customer.getAuthorities())) {
		return customer.getAuthorities()
						.stream()
						.map(auth -> new SimpleGrantedAuthority("ROLE_"+auth.getAuthority()))
						.collect(Collectors.toList());
		}
		else {
			return new ArrayList<>();
		}}
		catch (Exception e) {
			logger.error("", e);
			return new ArrayList<>();
		}
	}

}
