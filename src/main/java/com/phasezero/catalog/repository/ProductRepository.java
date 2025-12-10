package com.phasezero.catalog.repository;

import com.phasezero.catalog.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByPartNumber(String partNumber);

    List<Product> findByPartNameContainingIgnoreCase(String partName);

    List<Product> findByCategoryIgnoreCase(String category);
}
