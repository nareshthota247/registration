package com.example.demo.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.User;


@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, String> {
	
	User findByEmailIdIgnoreCase(String emailId);
//	List<AppUser> findByName(String name);

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.emailId = ?#{ principal?.emailId }")
    @Modifying
    @Transactional
    public void updateLastLogin(@Param("lastLogin") Date lastLogin);

}
