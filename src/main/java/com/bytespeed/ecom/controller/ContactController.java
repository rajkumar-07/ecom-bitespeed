package com.bytespeed.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bytespeed.ecom.dto.ContactReqDto;
import com.bytespeed.ecom.service.ContactService;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

	@Autowired
	ContactService contactService;
	
	@PostMapping("/identify")
	public ResponseEntity<Object> getContactDetail(@RequestBody ContactReqDto requestBody){
		return contactService.getOrAddContactDetail(requestBody);
	}
	
	@GetMapping("/find")
	public ResponseEntity<Object> findDetails(@RequestBody ContactReqDto request){
		return contactService.testDB(request);
	}
}
