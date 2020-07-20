package io.skymind.pathmind.shared.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PathmindNumberUtils {
    private PathmindNumberUtils() {
    }

    public static Number formatNumber(Double originalNumber) {
        if (originalNumber >= 10) {
            return (int) Math.rint(originalNumber);
        }
        BigDecimal bd = BigDecimal.valueOf(originalNumber);
        int newScale = 2-bd.precision()+bd.scale();
        return bd.setScale(newScale, RoundingMode.HALF_UP);
    }
}