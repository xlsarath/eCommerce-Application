package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private final static Logger log = LoggerFactory.getLogger(ItemControllerTest.class);
    private ItemController testItemController;
    private ItemRepository testItemRepository = mock(ItemRepository.class);
    private Item testItem;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        log.debug("test scenario : test base setup");
        testItemController = new ItemController();
        testItem = new Item();
        testItem.setId(0L);
        testItem.setName("test object");
        testItemRepository.save(testItem);
        TestUtil.injectObject(testItemController, "itemRepository", testItemRepository);
    }

    @Test
    public void fetchItems() throws Exception {
        log.debug("test scenario : retrieve items");
        final ResponseEntity<List<Item>> response = testItemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void retrieveItemsById() throws Exception {
        log.debug("test scenario : retrieve items by Id");
        when(testItemRepository.findById(0L)).thenReturn(java.util.Optional.ofNullable(testItem));
        final ResponseEntity<Item> response = testItemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void retrieveItemsByInvalidId() throws Exception {
        log.debug("test scenario : retrieve items invalid ID");
        final ResponseEntity<Item> response = testItemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void retrieveItemsByName() throws Exception {
        log.debug("test scenario : retrieve items by name");
        when(testItemRepository.findByName("test object")).thenReturn(Collections.singletonList(testItem));
        final ResponseEntity<List<Item>> response = testItemController.getItemsByName(testItem.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void retrieveItemsByNameInvalidCase() throws Exception {
        log.debug("test scenario : retrieve items by name invalid case");
        final ResponseEntity<List<Item>> response = testItemController.getItemsByName("test object");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
