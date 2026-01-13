package com.example.jayant.bookingservice.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

//CREATE TABLE customer (
//        id BIGINT AUTO_INCREMENT PRIMARY KEY,
//        name VARCHAR(255) NOT NULL,
//email VARCHAR(255) NOT NULL,
//address VARCHAR(255) NOT NULL
//);

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="customer")
public class Customer {

    @Id
    private Long id;
    private String name;
    private String email;
    private String address;

}
