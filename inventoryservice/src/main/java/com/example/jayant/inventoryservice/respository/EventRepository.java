package com.example.jayant.inventoryservice.respository;

import com.example.jayant.inventoryservice.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
