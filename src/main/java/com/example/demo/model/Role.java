package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Enumerated(EnumType.STRING)
    private AuthorityType name;
    
    public Role() {
    	
	}
    
    public Role(AuthorityType name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuthorityType getName() {
		return name;
	}

	public void setName(AuthorityType name) {
		this.name = name;
	}
    
    

    
    
}
