package com.seatsurfer.util;

import java.util.Locale;

public final class NameUtils {

    private NameUtils() {
    }

    public static String normalizePersonName(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    public static String seatLabel(int rowIndex, int columnIndex) {
        return rowLetters(rowIndex) + columnIndex;
    }

    private static String rowLetters(int rowIndex) {
        int current = Math.max(1, rowIndex);
        StringBuilder builder = new StringBuilder();
        while (current > 0) {
            current--;
            builder.insert(0, (char) ('A' + (current % 26)));
            current /= 26;
        }
        return builder.toString();
    }
}
