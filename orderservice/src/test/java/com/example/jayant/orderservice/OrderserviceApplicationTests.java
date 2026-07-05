package com.example.jayant.orderservice;

import com.example.jayant.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        "inventory.service.url=http://localhost:9999/api/v1/inventory",
        "spring.kafka.bootstrap-servers=localhost:9092"
})
class OrderserviceApplicationTests {

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private KafkaTemplate<?, ?> kafkaTemplate;

    @Test
    void contextLoads() {
    }
}
