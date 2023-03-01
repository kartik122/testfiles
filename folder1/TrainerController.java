package com.trainer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trainer.beans.Trainer;
import com.trainer.proxy.TrainerDetailsProxy;
import com.trainer.service.TrainerService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api")
public class TrainerController {

	@Autowired
	private TrainerService service;
	
	@Autowired
	private TrainerDetailsProxy trainerDetailsProxy;
	
	@GetMapping("/")
	public String health() {
		return "application running";
	}
	
	@PostMapping("/trainer")
	public ResponseEntity<?> saveUser( @RequestBody Trainer trainer) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(trainer));
	}
	
	@GetMapping("/trainer/{id}")
	public Trainer gettrainer(@PathVariable("id") Integer id) {
		return service.getTrainerByIdAsObject(id);
	}
	
	@PutMapping("trainer")
	public ResponseEntity<Trainer> updateCust (@RequestBody Trainer trainer) {
		if(service.getTrainerByIdA(trainer.getTrainerId()).isPresent()) {
			Trainer updatedCust = service.update(trainer);
			return ResponseEntity.status(HttpStatus.OK).body(updatedCust);
		}
		return null;
	}
	
	@DeleteMapping("/trainer/{id}")
	public ResponseEntity<String> deleteCust(@PathVariable("id") Integer id) {
		if(service.getTrainerByIdA(id).isPresent()) {
			service.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body("trainer Deleted " + id);
		}
		return null;
	}
	
	
	final String TRAINER_DETAILS_SERVICE="TRAINER_DETAILS_SERVICE";
	
	// making open feign request 
	@GetMapping("/trainer-details/{name}")
	@CircuitBreaker(name = TRAINER_DETAILS_SERVICE, fallbackMethod = "trainerDetailsAddressFallBack")
	
//	@RateLimiter()
//	@Retry()
	public String getUserAddress( @PathVariable String name) {
		return trainerDetailsProxy.getTrainerAddress(name);
	}
	
	// fallback method when trainer address is not found 
	public String custDetailsAddressFallBack(Exception e) {
		return "Sorry trainer Details Service Is Down";
	}
}
