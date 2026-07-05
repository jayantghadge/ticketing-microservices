package com.example.jayant.inventoryservice.controller;

import com.example.jayant.inventoryservice.response.EventInventoryResponse;
import com.example.jayant.inventoryservice.response.VenueInventoryResponse;
import com.example.jayant.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Test
    void inventoryGetAllEvents_ShouldReturnEvents() throws Exception {
        EventInventoryResponse event = EventInventoryResponse.builder()
                .eventId(100L).event("Concert").capacity(200L)
                .ticketPrice(BigDecimal.valueOf(50)).build();

        when(inventoryService.getAllEvents()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/v1/inventory/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value(100))
                .andExpect(jsonPath("$[0].event").value("Concert"))
                .andExpect(jsonPath("$[0].capacity").value(200));
    }

    @Test
    void inventoryByVenue_ShouldReturnVenue() throws Exception {
        VenueInventoryResponse venue = VenueInventoryResponse.builder()
                .venueId(1L).venueName("Stadium").totalCapacity(500L).build();

        when(inventoryService.getVenueInformation(1L)).thenReturn(venue);

        mockMvc.perform(get("/api/v1/inventory/venue/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.venueId").value(1))
                .andExpect(jsonPath("$.venueName").value("Stadium"));
    }

    @Test
    void inventoryForevent_ShouldReturnEvent() throws Exception {
        EventInventoryResponse event = EventInventoryResponse.builder()
                .eventId(100L).event("Concert").capacity(200L).build();

        when(inventoryService.getEventInventory(100L)).thenReturn(event);

        mockMvc.perform(get("/api/v1/inventory/event/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(100))
                .andExpect(jsonPath("$.event").value("Concert"));
    }

    @Test
    void updateEventCapacity_ShouldReturnOk() throws Exception {
        doNothing().when(inventoryService).updateEventCapacity(100L, 5L);

        mockMvc.perform(put("/api/v1/inventory/event/100/capacity/5"))
                .andExpect(status().isOk());

        verify(inventoryService).updateEventCapacity(100L, 5L);
    }
}
