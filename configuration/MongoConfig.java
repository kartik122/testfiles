package com.assessment.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.assessment.beans.Customer;
import com.assessment.repo.CustRepo;

@Configuration
@EnableMongoRepositories(basePackageClasses = CustRepo.class)
public class MongoConfig {

	
	@Bean
	public CommandLineRunner commandLineRunner(CustRepo repo) {
		return null;
	}  
	
}