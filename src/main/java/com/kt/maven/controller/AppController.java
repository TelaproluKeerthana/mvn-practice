package com.kt.maven.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kt.maven.models.Reco;
import com.kt.maven.service.AppService;

@RestController
public class AppController {
	
	AppService appService;
	
	public AppController(AppService service) {
		appService = service;
	}
	
	 @GetMapping("/hellomaven")
	    public String letsLearnMaven() {
			return "Hello Spring";
	    }
	 
	 
	 @PostMapping("/records")
	 public void recordCreation(@RequestBody Reco rec){
		 appService.createRecord(rec);
	 }
	 
	 @GetMapping("/records")
	 public List<Reco> getRecords(){
		 return appService.getAllRecords();
	 }
	 
	 
}
