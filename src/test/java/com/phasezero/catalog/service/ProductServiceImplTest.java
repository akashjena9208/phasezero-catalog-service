package com.phasezero.catalog.service;

import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.dto.ProductResponse;
import com.phasezero.catalog.exception.ProductAlreadyExistsException;

import com.phasezero.catalog.model.Product;
import com.phasezero.catalog.repository.ProductRepository;
import com.phasezero.catalog.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    private ProductRequest createValidRequest() {
        return new ProductRequest(
                "P-1001",
                "Hydraulic Filter",
                "filters",
                1200.50,
                10
        );
    }

    private Product createEntityFromRequest(ProductRequest request) {
        // Simulate what ProductMapper.toEntity does
        return Product.builder()
                .id(1L)
                .partNumber(request.partNumber().trim())
                .partName(request.partName().toLowerCase().trim())
                .category(request.category().trim())
                .price(request.price())
                .stock(request.stock())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void addProduct_success() {
        ProductRequest request = createValidRequest();

        when(productRepository.existsByPartNumber("P-1001")).thenReturn(false);

        Product saved = createEntityFromRequest(request);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse response = productService.addProduct(request);

        assertNotNull(response);
        assertEquals("P-1001", response.partNumber());
        assertEquals("hydraulic filter", response.partName());
        assertEquals("filters", response.category());
        assertEquals(1200.50, response.price());
        assertEquals(10, response.stock());

        verify(productRepository).existsByPartNumber("P-1001");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void addProduct_duplicatePartNumber_throwsException() {
        ProductRequest request = createValidRequest();

        when(productRepository.existsByPartNumber("P-1001")).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class,
                () -> productService.addProduct(request));

        verify(productRepository).existsByPartNumber("P-1001");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void addProduct_negativePrice_throwsIllegalArgumentException() {
        ProductRequest request = new ProductRequest(
                "P-1002",
                "Oil Filter",
                "filters",
                -10.0,
                5
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.addProduct(request)
        );

        assertEquals("price cannot be negative", ex.getMessage());
        verify(productRepository, never()).existsByPartNumber(anyString());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void addProduct_negativeStock_throwsIllegalArgumentException() {
        ProductRequest request = new ProductRequest(
                "P-1003",
                "Oil Filter",
                "filters",
                100.0,
                -5
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.addProduct(request)
        );

        assertEquals("stock cannot be negative", ex.getMessage());
        verify(productRepository, never()).existsByPartNumber(anyString());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAllProducts_returnsList() {
        Product p1 = Product.builder()
                .id(1L).partNumber("P-1001").partName("hydraulic filter")
                .category("filters").price(1200.50).stock(10)
                .createdAt(LocalDateTime.now())
                .build();

        Product p2 = Product.builder()
                .id(2L).partNumber("P-1002").partName("oil filter")
                .category("filters").price(800.00).stock(5)
                .createdAt(LocalDateTime.now())
                .build();

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProductResponse> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("P-1001", result.get(0).partNumber());
        assertEquals("P-1002", result.get(1).partNumber());
        verify(productRepository).findAll();
    }

    @Test
    void searchByName_returnsMatchingProducts() {
        Product p1 = Product.builder()
                .id(1L).partNumber("P-1001").partName("hydraulic filter")
                .category("filters").price(1200.50).stock(10)
                .createdAt(LocalDateTime.now())
                .build();

        when(productRepository.findByPartNameContainingIgnoreCase("filter"))
                .thenReturn(Collections.singletonList(p1));

        List<ProductResponse> result = productService.searchByName("filter");

        assertEquals(1, result.size());
        assertEquals("P-1001", result.get(0).partNumber());
        verify(productRepository).findByPartNameContainingIgnoreCase("filter");
    }

    @Test
    void filterByCategory_returnsMatchingProducts() {
        Product p1 = Product.builder()
                .id(1L).partNumber("P-1001").partName("hydraulic filter")
                .category("filters").price(1200.50).stock(10)
                .createdAt(LocalDateTime.now())
                .build();

        Product p2 = Product.builder()
                .id(2L).partNumber("P-1002").partName("oil filter")
                .category("filters").price(800.00).stock(5)
                .createdAt(LocalDateTime.now())
                .build();

        when(productRepository.findByCategoryIgnoreCase("filters"))
                .thenReturn(Arrays.asList(p1, p2));

        List<ProductResponse> result = productService.filterByCategory("filters");

        assertEquals(2, result.size());
        verify(productRepository).findByCategoryIgnoreCase("filters");
    }

    @Test
    void getTotalInventoryValue_returnsCorrectSum() {
        Product p1 = Product.builder()
                .id(1L).partNumber("P-1001").partName("hydraulic filter")
                .category("filters").price(100.0).stock(2)
                .createdAt(LocalDateTime.now())
                .build();

        Product p2 = Product.builder()
                .id(2L).partNumber("P-1002").partName("oil filter")
                .category("filters").price(50.0).stock(4)
                .createdAt(LocalDateTime.now())
                .build();

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        double value = productService.getTotalInventoryValue();

        // 100*2 + 50*4 = 200 + 200 = 400
        assertEquals(400.0, value);
        verify(productRepository).findAll();
    }
}
