package com.phasezero.catalog.dto;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String code,
        String message,
        Instant timestamp,
        T data
) {
    public static <T> ApiResponse<T> with(boolean success, String code, String message, Instant timestamp, T data) {
        return new ApiResponse<>(success, code, message, timestamp, data);
    }
}
