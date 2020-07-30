package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CartControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private CartController testCartController;
    private CartRepository testCartRepository = mock(CartRepository.class);
    private ItemRepository testItemRepository = mock(ItemRepository.class);
    private UserRepository testUserRepository = mock(UserRepository.class);
    private Item testItem;
    private Cart testCart;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        log.debug("Test Scenario : Init setup");
        testCartController = new CartController();
        testItem = new Item();
        testItem.setId(0L);
        testItem.setName("test obj");
        testItem.setDescription("test item description");
        testItem.setPrice(new BigDecimal("9.99"));
        testCart = new Cart();
        testCart.setItems(Collections.singletonList(testItem));
        TestUtil.injectObject(testCartController, "cartRepository", testCartRepository);
        TestUtil.injectObject(testCartController, "userRepository", testUserRepository);
        TestUtil.injectObject(testCartController, "itemRepository", testItemRepository);
    }

    @Test
    public void addToCart() throws Exception {
        log.debug("Test Scenario : Add to cart");
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("sarath");
        request.setItemId(0L);
        request.setQuantity(4);
        when(testUserRepository.findByUsername("sarath")).thenReturn(new User("sarath", "123456789", testCart));
        final ResponseEntity<Cart> response = testCartController.addTocart(request);
        assertNotNull(response);
    }

    @Test
    public void removeItemsFromCart() throws Exception {
        log.debug("Test Scenario : remove items from cart ");
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("sarath");
        request.setItemId(testItem.getId());
        request.setQuantity(4);
        final ResponseEntity<Cart> response = testCartController.removeFromcart(request);
        assertNotNull(response);
    }

}
