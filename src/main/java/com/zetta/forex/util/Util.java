package com.zetta.forex.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {

    public static double roundToNDecimals(double value, int n) {
        return BigDecimal.valueOf(value)
                .setScale(n, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static long cutToNDigits(long number, int n) {
        String numStr = Long.toString(number);

        if (numStr.length() <= n) {
            return number;
        }

        // Handle negative numbers (the '-' counts as a character)
        boolean isNegative = numStr.charAt(0) == '-';
        int endIndex = isNegative ? n + 1 : n; // Keep '-' plus 9 digits or 10 digits

        String truncatedStr = numStr.substring(0, endIndex);
        return Long.parseLong(truncatedStr);
    }
}
