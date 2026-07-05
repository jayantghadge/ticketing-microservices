package com.example.jayant.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.jayant.bookingservice.repository.CustomerRepository;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        "inventory.service.url=http://localhost:9999/api/v1/inventory",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
class BookingServiceApplicationTests {

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private KafkaTemplate<?, ?> kafkaTemplate;

    @Test
    void contextLoads() {
    }
}
