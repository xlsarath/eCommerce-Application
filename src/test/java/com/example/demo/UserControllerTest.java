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
    public void createUser() throws Exception {
        log.debug("Test Scenario : Create User With valid credentials");
        when(testBCryptEncoder.encode("Test_pass")).thenReturn("hashpass");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("Test_user");
        userRequest.setPassword("Test_pass");
        userRequest.setConfirmPassword("Test_pass");
        final ResponseEntity<User> response = testUserController.createUser(userRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Test_user", user.getUsername());
        assertEquals("hashpass", user.getPassword());
    }

    @Test
    public void createUserWithoutPassword() throws Exception {
        log.debug("Test Scenario : Create User Without Password");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user1");
        final ResponseEntity<User> response = testUserController.createUser(userRequest);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserInvalidPassword() throws Exception {
        log.debug("Test Scenario : Create User With invalid password");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user2");
        userRequest.setPassword("macha");
        userRequest.setConfirmPassword("macha");
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
        //assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserById() throws Exception {
        log.debug("Test Scenario : search user by userId");
        User defaultUser = new User();
        defaultUser.setId(0L);
        defaultUser.setUsername("sarath");
        when(testUserRepository.findById(0L)).thenReturn(java.util.Optional.of(defaultUser));
        final ResponseEntity<User> response = testUserController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assert user != null;
        assertEquals(0, user.getId());
        assertEquals("sarath", user.getUsername());
    }

    @Test
    public void findUserIDThatDoesntExist() throws Exception {
        log.debug("Test Scenario : search user by invalid userId");
        final ResponseEntity<User> response = testUserController.findById(0L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUsername() throws Exception {
        log.debug("Test Scenario : search user by username");
        User defaultUser = new User();
        defaultUser.setId(0L);
        defaultUser.setUsername("username");
        when(testUserRepository.findByUsername("username")).thenReturn(defaultUser);
        final ResponseEntity<User> response = testUserController.findByUserName("username");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assert user != null;
        assertEquals(0, user.getId());
        assertEquals("username", user.getUsername());
    }

    @Test
    public void findUserByUsernameInvalidCase() throws Exception {
        log.debug("Test Scenario : search user by username Invalid case");
        final ResponseEntity<User> response = testUserController.findByUserName("username");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}