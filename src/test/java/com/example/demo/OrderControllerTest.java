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

    public OrderControllerTest() {
    }

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {

        testOrderController = new OrderController();
        TestUtil.injectObject(testOrderController, "orderRepository", testOrderRepository);
        TestUtil.injectObject(testOrderController, "userRepository", testUserRepository);

        testCart = new Cart();
        testItem = new Item();
        testUser = new User("sarath", "pass");
        testItem.setId(0L);
        testItem.setName("sampleitem");
        testItem.setDescription("sample description");
        testItem.setPrice(new BigDecimal("9.99"));
        testCart.addItem(testItem);
        TestUtil.injectObject(testUser, "cart", testCart);

        testOrder = new UserOrder();
        testOrder.setId(0L);
        testOrder.setUser(testUser);
        testOrder.setItems(Arrays.asList(testItem, testItem, testItem));
        testOrder.setTotal(new BigDecimal("2.96"));
    }

    @Test
    public void submittingOrder() throws Exception {
        log.debug("Test Scenario : Submitting order");
        when(testUserRepository.findByUsername("sarath")).thenReturn(testUser);
        final ResponseEntity<UserOrder> response = testOrderController.submit(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, testCart.getItems().size());
        assertEquals(new BigDecimal("9.99"), testCart.getTotal());
    }

    @Test
    public void submittingOrderWithoutUser() throws Exception {
        log.debug("Test Scenario : Submitting order without user");
        final ResponseEntity<UserOrder> response = testOrderController.submit(null);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void submittingOrderWithoutItems() throws Exception {
        log.debug("Test Scenario : Submitting order without Items");
        when(testUserRepository.findByUsername("sarath")).thenReturn(testUser);
        testUser.getCart().removeItem(testItem);
        final ResponseEntity<UserOrder> response = testOrderController.submit(testUser.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, testCart.getItems().size());
    }

    @Test
    public void retrieveHistoryByUserName() throws Exception {
        log.debug("Test Scenario : retrieve History");
        when(testUserRepository.findByUsername("sarath")).thenReturn(null);
        final ResponseEntity<List<UserOrder>> response = testOrderController.getOrdersForUser(testUser.getUsername());
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void retriveHistoryNegative() throws Exception {
        log.debug("Test Scenario : retrieve History with invalid user params");
        final ResponseEntity<List<UserOrder>> response = testOrderController.getOrdersForUser(null);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}