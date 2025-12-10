package com.phasezero.catalog.service;

import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    List<ProductResponse> getAllProducts(int page, int size);


    List<ProductResponse> searchByName(String name);

    List<ProductResponse> filterByCategory(String category);

    List<ProductResponse> sortByPriceAscending();

    double getTotalInventoryValue();
}
