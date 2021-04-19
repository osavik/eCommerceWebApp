package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "userRepository", userRepository);

        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("password");
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(user.getCart());

        Item item = new Item();
        item.setId(0L);
        item.setDescription("description 1");
        item.setPrice(BigDecimal.valueOf(2.33));
        item.setName("item1");

        when(itemRepository.findById(0L)).thenReturn(Optional.of(item));
    }

    @Test
    public void test_find_cart_by_username(){

        ResponseEntity<Cart> response = cartController.findCartByUserName("test");

        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
    }


    @Test
    public void test_add_to_cart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertEquals(BigDecimal.valueOf(4.66), cart.getTotal());
        assertEquals("test", cart.getUser().getUsername());
        assertEquals(2, cart.getItems().size());

    }

    @Test
    public void test_remove_from_cart(){
        // 1. add to cart
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        // 2. remove from cart
        ModifyCartRequest removeCartRequest = new ModifyCartRequest();
        removeCartRequest.setItemId(0);
        removeCartRequest.setQuantity(1);
        removeCartRequest.setUsername("test");

        ResponseEntity<Cart> responseRemoved = cartController.removeFromcart(removeCartRequest);

        // 3. check

        assertEquals(200, responseRemoved.getStatusCodeValue());

        Cart cart = responseRemoved.getBody();
        assertEquals(BigDecimal.valueOf(2.33), cart.getTotal());
        assertEquals("test", cart.getUser().getUsername());
        assertEquals(1, cart.getItems().size());

    }
}
