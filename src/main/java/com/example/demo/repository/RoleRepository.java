package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.model.AuthorityType;
import com.example.demo.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByName(AuthorityType name);
}
