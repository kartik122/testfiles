package com.assessment.repo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.assessment.beans.Customer;

public interface CustRepo extends MongoRepository<Customer, Integer>{
	public List<Customer> findByName(String name);

	@Query("{'dob' : {$gt : ?0}, 'dob' : {$lt : ?0}}")	
	public List<Customer> findBetweenDob(Date dob1, Date dob2);
}
