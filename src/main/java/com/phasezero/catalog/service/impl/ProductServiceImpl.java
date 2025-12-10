package com.phasezero.catalog.service.impl;

import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.dto.ProductResponse;
import com.phasezero.catalog.exception.ProductAlreadyExistsException;
import com.phasezero.catalog.mapper.ProductMapper;
import com.phasezero.catalog.model.Product;
import com.phasezero.catalog.repository.ProductRepository;
import com.phasezero.catalog.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    @CacheEvict(value = { "sortedByPrice", "inventoryValue" }, allEntries = true)
    public ProductResponse addProduct(ProductRequest request) {
        log.info("Adding product with partNumber={}", request.partNumber());

        if (request.price() < 0) {
            log.warn("Rejected product {} due to negative price={}",
                    request.partNumber(), request.price());
            throw new IllegalArgumentException("price cannot be negative");
        }
        if (request.stock() < 0) {
            log.warn("Rejected product {} due to negative stock={}",
                    request.partNumber(), request.stock());
            throw new IllegalArgumentException("stock cannot be negative");
        }

        String partNumberTrimmed = request.partNumber().trim();

        if (productRepository.existsByPartNumber(partNumberTrimmed)) {
            log.warn("Product with partNumber={} already exists (pre-check)", partNumberTrimmed);
            throw new ProductAlreadyExistsException(
                    "Product with partNumber '" + partNumberTrimmed + "' already exists");
        }

        Product product = ProductMapper.toEntity(request);

        try {
            Product saved = productRepository.save(product);
            log.info("Product saved id={} partNumber={}", saved.getId(), saved.getPartNumber());
            return ProductMapper.toResponse(saved);
        } catch (DataIntegrityViolationException e) {
            log.error("DataIntegrityViolation when saving partNumber={}", partNumberTrimmed);
            throw new ProductAlreadyExistsException(
                    "Product with partNumber '" + partNumberTrimmed + "' already exists");
        }
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.debug("Fetching all products");
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> ProductMapper.toResponse(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchByName(String name) {
        log.debug("Searching products by name containing '{}'", name);
        List<Product> products = productRepository.findByPartNameContainingIgnoreCase(name);
        return products.stream()
                .map(product -> ProductMapper.toResponse(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> filterByCategory(String category) {
        log.debug("Filtering products by category='{}'", category);
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        return products.stream()
                .map(product -> ProductMapper.toResponse(product))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("sortedByPrice")
    public List<ProductResponse> sortByPriceAscending() {
        log.info("Fetching products sorted by price (may hit cache)");
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "price"));
        return products.stream()
                .map(product -> ProductMapper.toResponse(product))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("inventoryValue")
    public double getTotalInventoryValue() {
        log.info("Calculating total inventory value (may hit cache)");
        List<Product> products = productRepository.findAll();
        double value = products.stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();
        log.debug("Total inventory value={}", value);
        return value;
    }
}
