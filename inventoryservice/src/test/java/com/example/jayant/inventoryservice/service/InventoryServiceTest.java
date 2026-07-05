package com.example.jayant.inventoryservice.service;

import com.example.jayant.inventoryservice.entity.Event;
import com.example.jayant.inventoryservice.entity.Venue;
import com.example.jayant.inventoryservice.response.EventInventoryResponse;
import com.example.jayant.inventoryservice.response.VenueInventoryResponse;
import com.example.jayant.inventoryservice.respository.EventRepository;
import com.example.jayant.inventoryservice.respository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void getAllEvents_ShouldReturnAllEvents() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("Stadium");
        venue.setAddress("NYC");
        venue.setTotalCapacity(500L);

        Event event = new Event();
        event.setId(100L);
        event.setName("Concert");
        event.setTotalCapacity(500L);
        event.setLeftCapacity(200L);
        event.setVenue(venue);
        event.setTicketPrice(BigDecimal.valueOf(50));

        when(eventRepository.findAll()).thenReturn(List.of(event));

        List<EventInventoryResponse> result = inventoryService.getAllEvents();

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getEventId());
        assertEquals("Concert", result.get(0).getEvent());
        assertEquals(200L, result.get(0).getCapacity());
        assertEquals(BigDecimal.valueOf(50), result.get(0).getTicketPrice());
        assertNotNull(result.get(0).getVenue());
    }

    @Test
    void getEventInventory_ShouldReturnEvent() {
        Venue venue = new Venue();
        venue.setId(1L);

        Event event = new Event();
        event.setId(100L);
        event.setName("Concert");
        event.setLeftCapacity(200L);
        event.setVenue(venue);
        event.setTicketPrice(BigDecimal.valueOf(50));

        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));

        EventInventoryResponse result = inventoryService.getEventInventory(100L);

        assertEquals(100L, result.getEventId());
        assertEquals("Concert", result.getEvent());
        assertEquals(200L, result.getCapacity());
        assertEquals(BigDecimal.valueOf(50), result.getTicketPrice());
    }

    @Test
    void getEventInventory_ShouldThrow_WhenEventNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventoryService.getEventInventory(99L));
    }

    @Test
    void getVenueInformation_ShouldReturnVenue() {
        Venue venue = new Venue();
        venue.setId(1L);
        venue.setName("Stadium");
        venue.setTotalCapacity(500L);

        when(venueRepository.findById(1L)).thenReturn(Optional.of(venue));

        VenueInventoryResponse result = inventoryService.getVenueInformation(1L);

        assertEquals(1L, result.getVenueId());
        assertEquals("Stadium", result.getVenueName());
        assertEquals(500L, result.getTotalCapacity());
    }

    @Test
    void getVenueInformation_ShouldThrow_WhenVenueNotFound() {
        when(venueRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventoryService.getVenueInformation(99L));
    }

    @Test
    void updateEventCapacity_ShouldDecreaseCapacity() {
        Event event = new Event();
        event.setId(100L);
        event.setLeftCapacity(200L);

        when(eventRepository.findById(100L)).thenReturn(Optional.of(event));
        when(eventRepository.saveAndFlush(any())).thenReturn(event);

        inventoryService.updateEventCapacity(100L, 5L);

        assertEquals(195L, event.getLeftCapacity());
        verify(eventRepository).saveAndFlush(event);
    }

    @Test
    void updateEventCapacity_ShouldThrow_WhenEventNotFound() {
        when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventoryService.updateEventCapacity(99L, 5L));
    }
}
