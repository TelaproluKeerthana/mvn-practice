package com.kt.maven.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.kt.maven.models.Reco;
import com.kt.maven.repository.AppRepository;

@Service
public class AppService {
	AppRepository repo;
	public AppService(AppRepository repository){
		repo = repository;
	}
	
	public void createRecord(Reco rec) {
		repo.save(rec);
	}
	
	public List<Reco> getAllRecords(){
		return repo.findAll();
	}
}
