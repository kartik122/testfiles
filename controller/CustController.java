package com.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.beans.Customer;
import com.assessment.proxy.CustomerDetailsProxy;
import com.assessment.service.CustService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api")
public class CustController {

	@Autowired
	private CustService service;
	
	@Autowired
	private CustomerDetailsProxy custDetailsProxy;
	
	@GetMapping("/")
	public String health() {
		return "application running";
	}
	
	@PostMapping("/cust")
	public ResponseEntity<?> saveUser( @RequestBody Customer customer) {
		return ResponseEntity.(HttpStatus.CREATED).body(service.save(customer));
	}
	
	
	
	final String CUST_DETAILS_SERVICE="CUST_DETAILS_SERVICE";
	
	// making open feign request 
	@GetMapping("/cust-details/{name}")
	@CircuitBreaker(name = CUST_DETAILS_SERVICE, fallbackMethod = "custDetailsAddressFallBack")
	
//	@RateLimiter()
//	@Retry()
	public String getUserAddress( @PathVariable String name) {
		return custDetailsProxy.getCustAddress(name);
	}
	
	// fallback method when customer address is not found 
	public String custDetailsAddressFallBack(Exception e) {
		return "Sorry Customer Details Service Is Down";
	}
}
