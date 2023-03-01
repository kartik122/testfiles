package com.assessment.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.beans.Customer;
import com.assessment.repo.CustRepo;

/**
 * 
 * @author kartikgupta
 * @see the service layer invokes Repo layer for operations
 */

@Service
public class CustService {
	@Autowired
	private CustRepo repo; 
	
	public Customer save(Customer cust) {
		return repo.insert(cust);
	}
	
	public Customer update(Customer cust) {
		return repo.save(cust); 
	}
	public void delete(Integer id) {
		repo.deleteById(id);
	}
	
	public List<Customer> getAll() {
		return repo.findAll(); 
	}
	
	public Customer getCustByIdAsObject(Integer id) {
		Optional<Customer> byId = repo.findById(id);
		return byId.isPresent()?byId.get():null;
	}
	
	public Optional<Customer> getCustByIdA(Integer id) {
		return repo.findById(id);
	}
	
	public List<Customer> getCustByDobBetween(Date dob1, Date dob2) {
		return repo.findBetweenDob(dob1, dob2);
	}
}
