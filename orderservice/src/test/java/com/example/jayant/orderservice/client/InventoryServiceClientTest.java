package com.example.jayant.orderservice.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder builder;

    private InventoryServiceClient client;

    @BeforeEach
    void setUp() {
        when(builder.build()).thenReturn(restTemplate);
        client = new InventoryServiceClient(builder);
        ReflectionTestUtils.setField(client, "inventoryServiceUrl", "http://localhost:8080/api/v1/inventory");
    }

    @Test
    void updateInventory_ShouldCallCorrectUrl() {
        client.updateInventory(100L, 3L);

        verify(restTemplate).put("http://localhost:8080/api/v1/inventory/event/100/capacity/3", null);
    }
}
