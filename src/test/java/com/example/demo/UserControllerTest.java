package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.AuthenticationRequest;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class UserControllerTest {

    private final static Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    private UserController testUserController;
    private UserRepository testUserRepository = mock(UserRepository.class);
    private CartRepository testCartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder testBCryptEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        testUserController = new UserController();
        TestUtil.injectObject(testUserController, "userRepository", testUserRepository);
        TestUtil.injectObject(testUserController, "cartRepository", testCartRepository);
        TestUtil.injectObject(testUserController, "bCryptPasswordEncoder", testBCryptEncoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {
        log.debug("Running test: create_user_happy_path");
        log.debug("Stubbing password.");
        when(testBCryptEncoder.encode("test_password")).thenReturn("hashedPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test_user");
        userRequest.setPassword("test_password");
        userRequest.setConfirmPassword("test_password");
        final ResponseEntity<User> response = testUserController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test_user", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void create_user_no_password() throws Exception {
        log.debug("Running test: create_user_no_password");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test_user");
        final ResponseEntity<User> response = testUserController.createUser(userRequest);
        assertNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void create_user_bad_password() throws Exception {
        log.debug("Running test: create_user_bad_password");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test_user");
        userRequest.setPassword("passwo");
        userRequest.setConfirmPassword("passwo");
        final ResponseEntity<User> response = testUserController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void create_user_mismatch_password() throws Exception {
        log.debug("Running test: create_user_mismatch_password");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test_user");
        userRequest.setPassword("test_password");
        userRequest.setConfirmPassword("cool_password");
        final ResponseEntity<User> response = testUserController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_id_happy_path() throws Exception {
        log.debug("Running test: find_user_by_id_happy_path");
        User defaultUser = new User();
        defaultUser.setId(0L);
        defaultUser.setUsername("default_user");
        when(testUserRepository.findById(0L)).thenReturn(java.util.Optional.of(defaultUser));
        final ResponseEntity<User> response = testUserController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assert user != null;
        assertEquals(0, user.getId());
        assertEquals("default_user", user.getUsername());
    }

    @Test
    public void find_user_by_id_not_found() throws Exception {
        log.debug("Running test: find_user_by_id_not_found");
        final ResponseEntity<User> response = testUserController.findById(0L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_username_happy_path() throws Exception {
        log.debug("Running test: find_user_by_username_happy_path");
        User defaultUser = new User();
        defaultUser.setId(0L);
        defaultUser.setUsername("default_user");
        when(testUserRepository.findByUsername("default_user")).thenReturn(defaultUser);
        final ResponseEntity<User> response = testUserController.findByUserName("default_user");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assert user != null;
        assertEquals(0, user.getId());
        assertEquals("default_user", user.getUsername());
    }

    @Test
    public void find_user_by_username_not_found() throws Exception {
        log.debug("Running test: find_user_by_username_not_found");
        final ResponseEntity<User> response = testUserController.findByUserName("default_user");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}