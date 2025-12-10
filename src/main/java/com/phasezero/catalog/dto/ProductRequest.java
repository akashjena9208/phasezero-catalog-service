package com.phasezero.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        @NotBlank(message = "partNumber is required")
        String partNumber,

        @NotBlank(message = "partName is required")
        String partName,

        @NotBlank(message = "category is required")
        String category,

        @Min(value = 0, message = "price cannot be negative")
        double price,

        @Min(value = 0, message = "stock cannot be negative")
        int stock
) {}
