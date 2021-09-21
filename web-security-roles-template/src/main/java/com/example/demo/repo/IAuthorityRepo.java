package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AuthorityEntity;

@Repository
public interface IAuthorityRepo extends JpaRepository<AuthorityEntity	, Integer> {
	List<AuthorityEntity> findByAuthorityIn(Iterable<String> authority);
}
