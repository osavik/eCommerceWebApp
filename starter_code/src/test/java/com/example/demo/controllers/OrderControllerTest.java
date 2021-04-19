package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("password");

        Item item = new Item();
        item.setName("item1");
        item.setId(0L);
        item.setDescription("description item 1");
        item.setPrice(BigDecimal.valueOf(5.0));

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setItems(items);
        cart.setTotal(BigDecimal.valueOf(5.0));
        cart.setUser(user);

        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void test_submit(){
        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertNotNull(order);

        assertEquals(1, order.getItems().size());
        assertEquals(BigDecimal.valueOf(5.0), order.getTotal());
        assertEquals("test", order.getUser().getUsername());
    }

    @Test
    public void test_get_orders_for_user(){

        orderController.submit("test");

        ResponseEntity<List<UserOrder>> responseOrders = orderController.getOrdersForUser("test");

        assertEquals(200, responseOrders.getStatusCodeValue());

        List<UserOrder> userOrders = responseOrders.getBody();

        assertNotNull(userOrders);
    }

}
