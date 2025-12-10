package com.phasezero.catalog.controller;

import com.phasezero.catalog.dto.ApiResponse;
import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.dto.ProductResponse;
import com.phasezero.catalog.service.ProductService;
import com.phasezero.catalog.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Products", description = "Operations on the product catalog")
@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;


    @Operation(
            summary = "Add new product",
            description = "Creates a new product enforcing unique partNumber, lowercase partName, and non-negative price/stock."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.addProduct(request);
        ApiResponse<ProductResponse> body = ResponseUtil.created(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Operation(
            summary = "List all products with pagination",
            description = "Returns a paginated list of all products stored in the catalog. "
                    + "Use 'page' (0-based) and 'size' to control the result set."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<ProductResponse> list = productService.getAllProducts(page, size);
        ApiResponse<List<ProductResponse>> body = ResponseUtil.ok(list);
        return ResponseEntity.ok(body);
    }


    @Operation(
            summary = "Search products by name",
            description = "Search products whose partName contains the given text (case-insensitive)."
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> searchByName(
            @RequestParam("name") String name) {
        List<ProductResponse> list = productService.searchByName(name);
        ApiResponse<List<ProductResponse>> body = ResponseUtil.ok(list);
        return ResponseEntity.ok(body);
    }

    @Operation(
            summary = "Filter by category",
            description = "Return all products for a given category (case-insensitive match)."
    )
    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> filterByCategory(
            @RequestParam("category") String category) {
        List<ProductResponse> list = productService.filterByCategory(category);
        ApiResponse<List<ProductResponse>> body = ResponseUtil.ok(list);
        return ResponseEntity.ok(body);
    }

    @Operation(
            summary = "Sort products by price",
            description = "Return all products sorted by price in ascending order."
    )
    @GetMapping("/sorted-by-price")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProductsSortedByPrice() {
        List<ProductResponse> list = productService.sortByPriceAscending();
        ApiResponse<List<ProductResponse>> body = ResponseUtil.ok(list);
        return ResponseEntity.ok(body);
    }

    @Operation(
            summary = "Total inventory value",
            description = "Returns sum(price * stock) for all products."
    )
    @GetMapping("/inventory/value")
    public ResponseEntity<ApiResponse<Double>> getTotalInventoryValue() {
        double value = productService.getTotalInventoryValue();
        ApiResponse<Double> body = ResponseUtil.message("OK", "Total inventory value calculated", value);
        return ResponseEntity.ok(body);
    }
}
