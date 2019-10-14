package com.example.demo;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.UserMaintanceController;

@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest(UserMaintanceController.class)
@AutoConfigureMockMvc
public class RegistrationApplicationTests {

	@Autowired
    private MockMvc mvc;
	
	@Test
	public void contextLoads() {
	}
	
	@WithMockUser(value = "test@test.com")
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/secure/getUserInfo").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
	

}
