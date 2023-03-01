package com.assessment.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("CUSTOMER-DETAILS")
public interface CustomerDetailsProxy {
	@GetMapping("/customer-details/{name}")
	public String getCustAddress( @PathVariable String name);
}