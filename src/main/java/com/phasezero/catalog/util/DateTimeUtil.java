package com.phasezero.catalog.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateTimeUtil {

    private DateTimeUtil() {}

    public static Instant nowInstant() {
        return Instant.now();
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }
}
