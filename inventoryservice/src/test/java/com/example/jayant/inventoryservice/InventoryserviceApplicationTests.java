package com.example.jayant.inventoryservice;

import com.example.jayant.inventoryservice.respository.EventRepository;
import com.example.jayant.inventoryservice.respository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class InventoryserviceApplicationTests {

    @MockitoBean
    private EventRepository eventRepository;

    @MockitoBean
    private VenueRepository venueRepository;

	@Test
	void contextLoads() {
	}

}
