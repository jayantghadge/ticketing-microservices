package com.example.jayant.bookingservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueResponse {
    private Long Id;
    private String name;
    private String address;
    private VenueResponse venue;
    private Long totalCapacity;
}
