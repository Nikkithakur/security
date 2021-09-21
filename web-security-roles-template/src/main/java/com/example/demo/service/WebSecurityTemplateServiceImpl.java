package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.demo.entity.AuthorityEntity;
import com.example.demo.entity.CustomerEntity;
import com.example.demo.model.AuthorityDTO;
import com.example.demo.model.CustomerDTO;
import com.example.demo.repo.IAuthorityRepo;
import com.example.demo.repo.ICustomerRepo;

@Service
@Transactional
public class WebSecurityTemplateServiceImpl {

	private static Logger logger = LoggerFactory.getLogger(WebSecurityTemplateServiceImpl.class);

	@Autowired
	ICustomerRepo cusRepo;

	@Autowired
	IAuthorityRepo authRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostConstruct
	public void a() {
		AuthorityEntity entity = new AuthorityEntity();
		entity.setAuthority("ADMIN");
		AuthorityEntity save = authRepo.save(entity);
		CustomerEntity cusEntity = new CustomerEntity();
		cusEntity.setUsername("bruce");
		cusEntity.setEnabled(true);
		cusEntity.setAuthorities(Set.of(save));
		cusEntity.setPassword(passwordEncoder.encode("hello"));
		cusRepo.save(cusEntity);

	}

	public List<CustomerDTO> createCustomer(List<CustomerDTO> customersList) throws RoleNotFoundException {
		List<CustomerEntity> customerEntityList = new ArrayList<>();
		customersList.forEach(customer -> {
			CustomerEntity entity = new CustomerEntity();
			BeanUtils.copyProperties(customer, entity);
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
			Set<AuthorityEntity> authorities = entity.getAuthorities();
			List<AuthorityEntity> fetchedAuthorities = authRepo.findByAuthorityIn(customer.getAuthorities());
			if (!CollectionUtils.isEmpty(fetchedAuthorities)) {
				authorities.addAll(fetchedAuthorities);
			}
			customerEntityList.add(entity);
		});

		List<CustomerEntity> savedCustomers = cusRepo.saveAll(customerEntityList);
		logger.info("new customer created: {} ", savedCustomers);
		List<CustomerDTO> customerDTOList = new ArrayList<>();
		savedCustomers.forEach(entity -> customerDTOList.add(new CustomerDTO(entity.getCustomerId(),
				entity.getUsername(), entity.getPassword(), entity.isEnabled(),
				entity.getAuthorities().stream().map(AuthorityEntity::getAuthority).collect(Collectors.toSet()))));
		return customerDTOList;
	}

	public List<AuthorityDTO> createAuthority(List<AuthorityDTO> authoriesList) {
		List<AuthorityEntity> authorityEntities = new ArrayList<>();
		authoriesList.forEach(object -> {
			AuthorityEntity entity = new AuthorityEntity();
			BeanUtils.copyProperties(object, entity);
			authorityEntities.add(entity);

		});

		 List<AuthorityEntity> savedAuthorities = authRepo.saveAll(authorityEntities);
		logger.info("new authority created: {} ", savedAuthorities);
		List<AuthorityDTO> authorityDTOList = new ArrayList<>();
		savedAuthorities.forEach(entity -> {
			authorityDTOList.add(new AuthorityDTO(entity.getAuthority()));
		});
		return authorityDTOList;
	}

	public String deleteCustomer(Integer customerId) {
		Optional<CustomerEntity> cusOpt = cusRepo.findById(customerId);
		if (cusOpt.isPresent()) {
			cusRepo.delete(cusOpt.get());
			logger.info("customer deleted with id : {} ", customerId);
			return String.format("customer with id: {%s} and name: {%s} deleted succesfully", cusOpt.get().getCustomerId(),  cusOpt.get().getUsername());
		} else {
			logger.info("customer with id : {}  not found", customerId);
			return String.format("customer with id: {%s} not found", customerId);
		}
	}

	public String deleteAuthority(Integer authorityId) {
		Optional<AuthorityEntity> authOpt = authRepo.findById(authorityId);
		if (authOpt.isPresent()) {
			authRepo.delete(authOpt.get());
			logger.info("authority deleted with id : {} ", authorityId);
			return String.format("authority with id: {%s} and role: {%s} deleted succesfully", authOpt.get().getAuthorityId(), authOpt.get().getAuthority());
		} else {
			logger.info("authority with id : {}  not found", authorityId);
			return String.format("authority with id: {%s} not found", authorityId);
		}
	}
}
