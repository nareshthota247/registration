package com.example.demo.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -247690830249673108L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@Column(name="user_id")
	private Long id;
	
	private String emailId;
	
	private String password;
		
	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;
	
	private boolean isEnabled;
	
	private Date lastLogin;
	
//	@ManyToMany
//    @JoinTable( 
//        name = "users_roles", 
//        joinColumns = @JoinColumn(
//          name = "user_id", referencedColumnName = "id"), 
//        inverseJoinColumns = @JoinColumn(
//          name = "role_id", referencedColumnName = "id")) 
//    private Collection<Role> roles;
	
	@ManyToOne(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
    @JoinColumn(name="role")
    private Role role;
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
