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

    /**
     * Format the <code>orignalNumber</code> based on the <code>refNumber</code>.
     * <p>
     * This is used for formatting the simulation metric value which shows at the end of the training along with the uncertainty value.
     * <p>
     * For detailed explanation of the calculations involved, please visit <a href="https://github.com/SkymindIO/pathmind-webapp/wiki/Calculations-for-formatting-metrics-with-&-without-uncertainty-value-on-UI">the GitHub wiki page</a>.
     * @param originalNumber
     *            the original number to be formatted.
     * @param refNumber
     *            the ref number with its designated number of significant figures which doesn't require further formatting.
     * @param refNumberSigFig
     *            is used for determining the actual sig. fig. of figures ending with 0 at its last position.
     */
    public static String setSigFigBasedOnAnotherDouble(Double originalNumber, Double refNumber, int refNumberSigFig) {
        BigDecimal _refNumber;
        BigDecimal _originalNumber = BigDecimal.valueOf(originalNumber);
        String refNumber2SigFig = formatToSigFig(refNumber, 2);
        if (refNumberSigFig < 1) {
            return "";
        }
        if (refNumber2SigFig.contains(".")) {
            _refNumber = BigDecimal.valueOf(Double.parseDouble(refNumber2SigFig));
        } else {
            _refNumber = BigDecimal.valueOf(Integer.parseInt(refNumber2SigFig));
        }
        int originalNumberPrecision = _originalNumber.precision();
        int originalNumberScale = _originalNumber.scale();
        int originalNumberNonDecimalDigits =  getNumberOfNonDecimalDigits(originalNumberPrecision, originalNumberScale);
        int refNumberInsignificantPrecision = getInsignificantPrecision(_refNumber.precision(), refNumberSigFig);
        int sigFig = originalNumberNonDecimalDigits + _refNumber.scale() - refNumberInsignificantPrecision;
        if (_refNumber.signum() == 0) {
            /* This is needed because BigDecimal always saves 0 as 0.0 with 1 precision and 1 scale
             * which would mess up the calculation.
             */
            sigFig -= _refNumber.scale();
        }
        int newScale = sigFig - originalNumberPrecision + originalNumberScale;
        _originalNumber = _originalNumber.setScale(newScale, RoundingMode.HALF_EVEN);
        return _originalNumber.toPlainString().replace(",", "");
    }

    public static String formatToSigFig(Double originalNumber, int sigFig) {
        MathContext mathContext = new MathContext(sigFig, RoundingMode.HALF_EVEN);
        BigDecimal formatted = new BigDecimal(originalNumber, mathContext);
        int numberOfNonDecimalDigits = getNumberOfNonDecimalDigits(formatted.precision(), formatted.scale());
        String result = formatted.toPlainString();
        if (formatted.precision() < sigFig) {
            if (numberOfNonDecimalDigits == formatted.precision()) {
                result += ".";
            }
            result += "0";
        }
        return result;
    }

    public static int getNumberOfNonDecimalDigits(int bdPrecision, int bdScale) {
        return bdPrecision - bdScale;
    }

    public static int getInsignificantPrecision(int bdPrecision, int sigFig) {
        return bdPrecision - sigFig;
    }
}