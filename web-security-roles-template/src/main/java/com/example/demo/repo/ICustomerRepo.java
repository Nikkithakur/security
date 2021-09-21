package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CustomerEntity;


@Repository
public interface ICustomerRepo  extends JpaRepository<CustomerEntity, Integer>{
	Optional<CustomerEntity> findByusername(String userName);

}
