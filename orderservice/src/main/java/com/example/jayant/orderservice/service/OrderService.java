package com.example.jayant.orderservice.service;


import com.example.jayant.bookingservice.event.BookingEvent;
import com.example.jayant.orderservice.client.InventoryServiceClient;
import com.example.jayant.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.example.jayant.orderservice.entity.Order;


@Service
@Slf4j
public class OrderService {


    private OrderRepository orderRepository;
    private InventoryServiceClient inventoryServiceClient;

    @Autowired
    public  OrderService(OrderRepository orderRepository,
                         InventoryServiceClient inventoryServiceClient){
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = "booking", groupId = "Order-service")
    public void orderEvent(BookingEvent bookingEvent){
        log.info("Received order event: {}", bookingEvent);

        //Create order object
        Order order = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);

        //Update Inventory
        inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());
        log.info("Inventory updated for event: {}, less ticketsL {}", order.getEventId(), order.getTicketCount());
        //Create InventoryClient
    }

    private Order createOrder(BookingEvent bookingEvent){
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

}
