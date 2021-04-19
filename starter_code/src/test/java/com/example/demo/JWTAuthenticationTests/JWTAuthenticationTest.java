package com.example.demo.JWTAuthenticationTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class JWTAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test_unauthorized_access() throws Exception {
        mvc.perform(get("/api/cart/details/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void test_create_login_check_authentication() throws Exception {

        String bodyCreate = "{\"username\": \"test\", \"password\": \"abcdefg\", \"confirmPassword\": \"abcdefg\"}";

        // 1. create user
        mvc.perform(post("/api/user/create").content(bodyCreate).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

        // 2. login user
        String loginBody = "{\"username\": \"test\", \"password\": \"abcdefg\"}";

        MvcResult result = mvc.perform(post("/login")
                .content(loginBody).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andReturn();

        String token = result.getResponse().getHeader("Authorization");

        // System.out.println(token);

        // 3. get user with token
        mvc.perform(get("/api/user/test")
                .header("Authorization", token)).andExpect(status().isOk());

    }

}
