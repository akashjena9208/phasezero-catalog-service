package com.phasezero.catalog.util;

import java.util.Locale;

public final class StringUtil {

    private StringUtil() {}

    public static String normalizeName(String value) {
        if (value == null) return null;
        return value.trim().toLowerCase(Locale.ROOT);
    }

    public static String trim(String value) {
        return value == null ? null : value.trim();
    }
}
