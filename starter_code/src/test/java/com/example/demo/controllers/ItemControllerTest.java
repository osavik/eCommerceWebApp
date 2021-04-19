package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);
    List<Item> allItems = new ArrayList<>();

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        // add items to the list
        allItems.add(createItem(0L, "item1", BigDecimal.valueOf(5L),"item 1 description"));
        allItems.add(createItem(1L, "item2", BigDecimal.valueOf(10L),"item 2 description"));


        // mock functionality of the ItemRepo
        when(itemRepository.findAll()).thenReturn(allItems);

        when(itemRepository.findById(0L)).thenReturn(Optional.of(allItems.get(0)));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(allItems.get(1)));

        when(itemRepository.findByName("item1")).thenReturn(allItems.subList(0,1));
        when(itemRepository.findByName("item2")).thenReturn(allItems.subList(1,2));
    }

    @Test
    public void test_get_items(){

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(200, response.getStatusCodeValue());

        List <Item> items = response.getBody();

        assertNotNull(items);
        assertEquals(2, items.size());

        assertEquals(allItems.get(0).getName(), items.get(0).getName());
        assertEquals(allItems.get(0).getId(), items.get(0).getId());

        assertEquals(allItems.get(1).getName(), items.get(1).getName());
        assertEquals(allItems.get(1).getId(), items.get(1).getId());

    }

    @Test
    public void test_get_item_by_id(){
        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(allItems.get(0).getName(), item.getName());
        assertEquals(allItems.get(0).getDescription(), item.getDescription());
        assertEquals(allItems.get(0).getPrice(), item.getPrice());
        assertEquals(allItems.get(0).getId(), item.getId());
    }

    @Test
    public void test_get_items_by_name(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item2");

        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());

        assertEquals(allItems.get(1).getName(), items.get(0).getName());
        assertEquals(allItems.get(1).getDescription(), items.get(0).getDescription());
        assertEquals(allItems.get(1).getPrice(), items.get(0).getPrice());
        assertEquals(allItems.get(1).getId(), items.get(0).getId());
    }


    private Item createItem(Long id, String name, BigDecimal price, String description) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(description);

        return item;
    }


}
