package io.skymind.pathmind.shared.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class PathmindNumberUtils {
    private PathmindNumberUtils() {
    }

    /**
     * Strip decimals off numbers >=10 and round them.
     * For numbers < 10, show 2 sig. fig. at most.
     * @param originalNumber
     *            the original number to be formatted
    */
    public static String formatNumber(Double originalNumber) {
        if (originalNumber >= 10) {
            return String.valueOf((int) Math.rint(originalNumber));
        }
        BigDecimal bd = BigDecimal.valueOf(originalNumber);
        int newScale = 2-bd.precision()+bd.scale();
        return bd.setScale(newScale, RoundingMode.HALF_UP).toString();
    }

    public static String setSigFigBasedOnAnotherDouble(Double originalNumber, Double refNumber, int refNumberSigFig) {
        BigDecimal bdRefNumber;
        BigDecimal bdOriginalNumber = BigDecimal.valueOf(originalNumber);
        String refNumber2SigFig = formatToSigFig(refNumber, 2);
        if (refNumber2SigFig.contains(".")) {
            bdRefNumber = BigDecimal.valueOf(Double.parseDouble(refNumber2SigFig));
        } else {
            bdRefNumber = BigDecimal.valueOf(Integer.parseInt(refNumber2SigFig));
        }
        int bdOriginalNumberPrecision = bdOriginalNumber.precision();
        int bdOriginalNumberScale = bdOriginalNumber.scale();
        int bdOriginalNumberNonDecimalDigits = bdOriginalNumberPrecision - bdOriginalNumberScale;
        int bdRefNumberPrecision = bdRefNumber.precision();
        int bdRefNumberScale = bdRefNumber.scale();
        int bdRefNumberActualPrecision = bdRefNumberPrecision - refNumberSigFig;
        int sigFig = bdOriginalNumberNonDecimalDigits + bdRefNumberScale - bdRefNumberActualPrecision;
        int newScale = sigFig - bdOriginalNumberPrecision + bdOriginalNumberScale;
        bdOriginalNumber = bdOriginalNumber.setScale(newScale, RoundingMode.HALF_EVEN);
        return bdOriginalNumber.toPlainString().replace(",", "");
    }

    public static String formatToSigFig(Double originalNumber, int sigFig) {
        MathContext mathContext = new MathContext(sigFig, RoundingMode.HALF_EVEN);
        BigDecimal formatted = new BigDecimal(originalNumber, mathContext);
        return formatted.toPlainString();
    }

    public static int powerOfTen(int pow) {
        final int[] POWERS_OF_10 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
        return POWERS_OF_10[pow];
    }
}