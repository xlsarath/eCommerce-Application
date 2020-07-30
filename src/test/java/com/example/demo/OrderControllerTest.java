package com.example.demo;
import com.example.demo.TestUtil;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private final static Logger log = LoggerFactory.getLogger(OrderControllerTest.class);
    private OrderController testOrderController;
    private OrderRepository testOrderRepository = mock(OrderRepository.class);
    private UserRepository testUserRepository = mock(UserRepository.class);
    private User testUser;
    private Cart testCart;
    private Item testItem;
    private UserOrder testOrder;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {

        // set up our controller and mocks
        testOrderController = new OrderController();
        TestUtil.injectObject(testOrderController, "orderRepository", testOrderRepository);
        TestUtil.injectObject(testOrderController, "userRepository", testUserRepository);

        // create some reusable dummy data
        testUser = new User("john_smith", "password");
        testCart = new Cart();
        testItem = new Item();
        testItem.setId(0L);
        testItem.setName("Test Item");
        testItem.setDescription("A really generic test item.");
        testItem.setPrice(new BigDecimal("2.99"));
        testCart.addItem(testItem);
        TestUtil.injectObject(testUser, "cart", testCart);

        // create test order dummy data
        testOrder = new UserOrder();
        testOrder.setId(0L);
        testOrder.setUser(testUser);
        testOrder.setItems(Arrays.asList(testItem, testItem, testItem));
        testOrder.setTotal(new BigDecimal("8.97"));
    }

    @Test
    public void submit_order_happy_path() throws Exception {
        log.debug("Running test: submit_order_happy_path.");
        log.debug("Stubbing user.");
        when(testUserRepository.findByUsername("john_smith")).thenReturn(testUser);
        final ResponseEntity<UserOrder> response = testOrderController.submit(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, testCart.getItems().size());
        assertEquals(new BigDecimal("2.99"), testCart.getTotal());
    }

    @Test
    public void submit_order_no_user() throws Exception {
        log.debug("Running test: submit_order_no_user.");
        final ResponseEntity<UserOrder> response = testOrderController.submit(null);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void submit_order_no_items() throws Exception {
        log.debug("Running test: submit_order_no_items.");
        log.debug("Stubbing user.");
        when(testUserRepository.findByUsername("john_smith")).thenReturn(testUser);
        testUser.getCart().removeItem(testItem);
        final ResponseEntity<UserOrder> response = testOrderController.submit(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, testCart.getItems().size());
    }

    @Test
    public void get_history_happy_path() throws Exception {
        log.debug("Running test: get_history_happy_path.");
        log.debug("Stubbing user.");
        when(testUserRepository.findByUsername("john_smith")).thenReturn(null);
        final ResponseEntity<List<UserOrder>> response = testOrderController.getOrdersForUser(testUser.getUsername());
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void get_history_no_user() throws Exception {
        log.debug("Running test: get_history_no_user.");
        final ResponseEntity<List<UserOrder>> response = testOrderController.getOrdersForUser(null);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}