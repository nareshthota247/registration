package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.Privilege;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
	
	Privilege findByName(String name);

}
