package com.example.jayant.bookingservice.service;

import com.example.jayant.bookingservice.client.InventoryServiceClient;
import com.example.jayant.bookingservice.entity.Customer;
import com.example.jayant.bookingservice.event.BookingEvent;
import com.example.jayant.bookingservice.repository.CustomerRepository;
import com.example.jayant.bookingservice.request.BookingRequest;
import com.example.jayant.bookingservice.response.BookingResponse;
import com.example.jayant.bookingservice.response.InventoryResponse;
import com.example.jayant.bookingservice.response.VenueResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private InventoryServiceClient inventoryServiceClient;

    @Mock
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_ShouldSucceed_WhenInventoryAvailable() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L).eventId(100L).ticketCount(2L).build();

        Customer customer = Customer.builder()
                .id(1L).name("John").email("john@test.com").address("NYC").build();

        InventoryResponse inventory = InventoryResponse.builder()
                .eventId(100L).event("Concert").capacity(10L)
                .ticketPrice(BigDecimal.valueOf(50)).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(inventoryServiceClient.getInventory(100L)).thenReturn(inventory);

        BookingResponse response = bookingService.createBooking(request);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(100L, response.getEventId());
        assertEquals(2L, response.getTicketCount());
        assertEquals(BigDecimal.valueOf(100), response.getTotalPrice());

        ArgumentCaptor<BookingEvent> captor = ArgumentCaptor.forClass(BookingEvent.class);
        verify(kafkaTemplate).send(eq("booking"), captor.capture());
        BookingEvent event = captor.getValue();
        assertEquals(1L, event.getUserId());
        assertEquals(100L, event.getEventId());
        assertEquals(2L, event.getTicketCount());
        assertEquals(BigDecimal.valueOf(100), event.getTotalPrice());
    }

    @Test
    void createBooking_ShouldThrow_WhenUserNotFound() {
        BookingRequest request = BookingRequest.builder()
                .userId(99L).eventId(100L).ticketCount(2L).build();

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void createBooking_ShouldThrow_WhenInsufficientInventory() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L).eventId(100L).ticketCount(20L).build();

        Customer customer = Customer.builder()
                .id(1L).name("John").email("john@test.com").address("NYC").build();

        InventoryResponse inventory = InventoryResponse.builder()
                .eventId(100L).event("Concert").capacity(10L)
                .ticketPrice(BigDecimal.valueOf(50)).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(inventoryServiceClient.getInventory(100L)).thenReturn(inventory);

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void createBooking_ShouldThrow_WhenInventoryServiceReturnsNull() {
        BookingRequest request = BookingRequest.builder()
                .userId(1L).eventId(100L).ticketCount(2L).build();

        Customer customer = Customer.builder()
                .id(1L).name("John").email("john@test.com").address("NYC").build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(inventoryServiceClient.getInventory(100L)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> bookingService.createBooking(request));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }
}
