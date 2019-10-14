package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.AuthorityType;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Component
public class InitialDataLoader implements
  ApplicationListener<ContextRefreshedEvent> {
 
    boolean alreadySetup = false;
 
    @Autowired
    private UserRepository userRepository;
  
    @Autowired
    private RoleRepository roleRepository;
  
    @Autowired
    private PasswordEncoder passwordEncoder;
  
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
  
        if (alreadySetup)
            return;
        
        createRoleIfNotFound(AuthorityType.ROLE_ADMIN);
        createRoleIfNotFound(AuthorityType.ROLE_USER);
 
        Role adminRole = roleRepository.findByName(AuthorityType.ROLE_ADMIN);
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmailId("test@test.com");
        user.setRole(adminRole);
        user.setEnabled(true);
        userRepository.save(user);
 
        alreadySetup = true;
    }
 
 
    @Transactional
    public Role createRoleIfNotFound(AuthorityType type) {
  
        Role role = roleRepository.findByName(type);
        if (role == null) {
            role = new Role(type);
            roleRepository.save(role);
        }
        return role;
    }
}
