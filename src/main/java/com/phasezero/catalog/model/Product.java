package com.phasezero.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(name = "uk_part_number", columnNames = "part_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_number", nullable = false, length = 100)
    private String partNumber;

    @Column(name = "part_name", nullable = false, length = 255)
    private String partName;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
