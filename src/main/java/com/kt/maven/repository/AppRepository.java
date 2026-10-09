package com.kt.maven.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.maven.models.Reco;

public interface AppRepository extends JpaRepository<Reco,Integer>{
}
