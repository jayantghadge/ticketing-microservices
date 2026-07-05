package com.example.jayant.bookingservice.controller;

import com.example.jayant.bookingservice.request.BookingRequest;
import com.example.jayant.bookingservice.response.BookingResponse;
import com.example.jayant.bookingservice.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void createBooking_ShouldReturnBookingResponse() throws Exception {
        BookingRequest request = BookingRequest.builder()
                .userId(1L).eventId(100L).ticketCount(2L).build();

        BookingResponse response = BookingResponse.builder()
                .userId(1L).eventId(100L).ticketCount(2L)
                .totalPrice(BigDecimal.valueOf(100))
                .build();

        when(bookingService.createBooking(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.eventId").value(100))
                .andExpect(jsonPath("$.ticketCount").value(2))
                .andExpect(jsonPath("$.totalPrice").value(100));
    }

    @Test
    void createBooking_ShouldReturn400_WhenInvalidBody() throws Exception {
        mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}
