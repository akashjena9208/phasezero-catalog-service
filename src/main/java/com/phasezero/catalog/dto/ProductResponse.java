package com.phasezero.catalog.dto;

public record ProductResponse(
        Long id,
        String partNumber,
        String partName,
        String category,
        double price,
        int stock
) {}
