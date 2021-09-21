package com.example.demo.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authorities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthorityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authority_id")
	private Integer authorityId;

	@Column(nullable = false, unique = true)
	private String authority;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "customer_authority_jointable", joinColumns = {
			@JoinColumn(name = "authority_id") }, inverseJoinColumns = { @JoinColumn(name = "customer_id") })
	Set<CustomerEntity> customers = new HashSet<>();
	
	public void removeCustomerRelation() {
		this.customers.forEach(cust -> cust.getAuthorities().remove(this));
		this.getCustomers().clear();
	}

	@Override
	public String toString() {
		return "AuthorityEntity [authorityId=" + authorityId + ", authority=" + authority + "]";
	}
	
}
