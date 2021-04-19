package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void create_user_happy_path(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void create_user_unhappy_path(){
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        // passwords ARE different
        r.setPassword("testPassword");
        r.setConfirmPassword("testPasswordANOTHER");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        r.setUsername("test");
        // passwords ARE too short
        r.setPassword("1");
        r.setConfirmPassword("1");

        response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }


    @Test
    public void test_find_user_by_username(){
        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");

        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("test");

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("password", u.getPassword());
    }

    @Test
    public void test_find_user_by_id(){
        User user = new User();
        user.setId(0);
        user.setUsername("test");
        user.setPassword("password");

        when(userRepository.findById(0L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(0L);

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("password", u.getPassword());
    }

}
