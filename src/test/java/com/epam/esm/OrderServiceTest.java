package com.epam.esm;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private GiftCertificateService giftCertificateService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        Long userId = 1L;
        Long giftCertificateId = 2L;

        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(giftCertificateId);
        giftCertificate.setName("Test Certificate");
        giftCertificate.setPrice(BigDecimal.TEN);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(giftCertificateService.findById(giftCertificateId)).thenReturn(Optional.of(giftCertificate));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.create(userId, giftCertificateId);

        assertNotNull(createdOrder);
        assertEquals(user, createdOrder.getUser());
        assertEquals(giftCertificate, createdOrder.getGiftCertificate());
        assertNotNull(createdOrder.getOrderedTime());
        assertEquals(BigDecimal.TEN, createdOrder.getPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> foundOrders = orderService.findAll();

        assertNotNull(foundOrders);
        assertEquals(2, foundOrders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindOrderById() {
        Long id = 1L;
        Order order = new Order();
        order.setId(id);
        order.setPrice(BigDecimal.TEN);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.findById(id);

        assertTrue(foundOrder.isPresent());
        assertEquals(BigDecimal.TEN, foundOrder.get().getPrice());
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void testFindOrdersInfoByUserIdWithPage() {
        Long userId = 1L;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findOrdersInfoByUserIdWithPage(userId, page, size)).thenReturn(new ArrayList<>());

        List<Order> result = orderService.findOrdersInfoByUserIdWithPage(userId, page, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findOrdersInfoByUserIdWithPage(userId, page, size);
    }

    @Test
    void testFindOrdersInfoByUserId() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findOrdersInfoByUserId(userId)).thenReturn(new ArrayList<>());

        List<Order> result = orderService.findOrdersInfoByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findOrdersInfoByUserId(userId);
    }
}
