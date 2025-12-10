package com.phasezero.catalog.util;

import com.phasezero.catalog.dto.ApiResponse;

public final class ResponseUtil {

    private ResponseUtil() {}

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.with(true, "OK", "Request successful", DateTimeUtil.nowInstant(), data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.with(true, "CREATED", "Resource created", DateTimeUtil.nowInstant(), data);
    }

    public static <T> ApiResponse<T> message(String code, String message, T data) {
        return ApiResponse.with(true, code, message, DateTimeUtil.nowInstant(), data);
    }
}
