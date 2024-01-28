package com.epam.esm;

import com.epam.esm.config.security.CustomeUserDetails;
import com.epam.esm.dto.OrderDTO;
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
import org.springframework.security.core.Authentication;

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

        OrderDTO orderDTO = new OrderDTO(giftCertificateId);

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(giftCertificateService.findById(giftCertificateId)).thenReturn(Optional.of(giftCertificate));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order createdOrder = orderService.createOrder(orderDTO, mockAuthentication());

        assertNotNull(createdOrder);
        assertEquals(user, createdOrder.getUser());
        assertEquals(giftCertificate, createdOrder.getGiftCertificate());
        assertNotNull(createdOrder.getOrderedTime());
        assertEquals(BigDecimal.TEN, createdOrder.getPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindOrdersInfoByUserId() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setUsername("testUser");

        when(userService.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findOrdersInfoByUserId(userId)).thenReturn(new ArrayList<>());

        List<Order> result = orderService.findOrdersByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findOrdersInfoByUserId(userId);
    }

    private Authentication mockAuthentication() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        CustomeUserDetails userDetails = new CustomeUserDetails(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        return authentication;
    }

}
