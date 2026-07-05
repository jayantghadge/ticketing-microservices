package com.example.jayant.orderservice.service;

import com.example.jayant.bookingservice.event.BookingEvent;
import com.example.jayant.orderservice.client.InventoryServiceClient;
import com.example.jayant.orderservice.entity.Order;
import com.example.jayant.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryServiceClient inventoryServiceClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void orderEvent_ShouldCreateOrderAndUpdateInventory() {
        BookingEvent event = BookingEvent.builder()
                .userId(1L).eventId(100L).ticketCount(2L)
                .totalPrice(BigDecimal.valueOf(100))
                .build();

        Order savedOrder = Order.builder()
                .id(1L).customerId(1L).eventId(100L)
                .ticketCount(2L).totalPrice(BigDecimal.valueOf(100))
                .build();

        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(savedOrder);

        orderService.orderEvent(event);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).saveAndFlush(captor.capture());
        Order order = captor.getValue();
        assertEquals(1L, order.getCustomerId());
        assertEquals(100L, order.getEventId());
        assertEquals(2L, order.getTicketCount());
        assertEquals(BigDecimal.valueOf(100), order.getTotalPrice());

        verify(inventoryServiceClient).updateInventory(100L, 2L);
    }
}
