package com.phasezero.catalog.mapper;

import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.dto.ProductResponse;
import com.phasezero.catalog.model.Product;
import com.phasezero.catalog.util.DateTimeUtil;
import com.phasezero.catalog.util.StringUtil;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductRequest request) {
        return Product.builder()
                .partNumber(StringUtil.trim(request.partNumber()))
                .partName(StringUtil.normalizeName(request.partName()))
                .category(StringUtil.trim(request.category()))
                .price(request.price())
                .stock(request.stock())
                .createdAt(DateTimeUtil.now())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getPartNumber(),
                product.getPartName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
        );
    }
}
