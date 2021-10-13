package io.skymind.pathmind.shared.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class PathmindNumberUtils {

    public static final String SPACED_PLUS_MINUS = "\u0020\u00B1\u0020";

    private PathmindNumberUtils() {
    }

    /**
     * Calculate uncertainty with the uncertainty being 2 sd.
     * We may want to change it to Inter-Quartile Range in the future depending on what the users find useful.
     */
    public static String calculateUncertainty(List<Double> list) {
        double average = list.stream().mapToDouble(Double::doubleValue).summaryStatistics().getAverage();
        double variance = calculateVariance(list);
        return calculateUncertainty(average, variance);
    }

    public static String calculateUncertainty(double avg, double variance) {
        double sd = Double.parseDouble(formatToSigFig(Math.sqrt(variance), 2));
        double uncertainty = 2 * sd;
        return addThousandsSeparatorToNumber(setSigFigBasedOnAnotherDouble(avg, uncertainty, 2)) + SPACED_PLUS_MINUS + addThousandsSeparatorToNumber(formatToSigFig(uncertainty, 2));
    }

    /**
     * Calculates the variance of the samples based on the List of Double
     */
    public static double calculateVariance(List<Double> list) {
        DoubleSummaryStatistics stat = list.stream().mapToDouble(Double::doubleValue).summaryStatistics();
        if (stat.getCount() <= 1) {
            return 0;
        }
        double sumOfDifferences = 0.0;
        for (int i = 0; i < stat.getCount(); i++) {
            sumOfDifferences += Math.pow((list.get(i) - stat.getAverage()), 2);
        }
        return sumOfDifferences / (double) (stat.getCount() - 1);
    }

    /**
     * Strip decimals off numbers >=10 and round them.
     * For numbers < 10, show 2 sig. fig. at most.
     *
     * @param originalNumber the original number to be formatted
     */
    public static String formatNumber(Double originalNumber) {
        if (originalNumber >= 10) {
            return addThousandsSeparatorToNumber(String.valueOf((int) Math.rint(originalNumber)));
        }
        BigDecimal bd = BigDecimal.valueOf(originalNumber);
        int newScale = 2 - bd.precision() + bd.scale();
        return bd.setScale(newScale, RoundingMode.HALF_UP).toString();
    }

    /**
     * Format the <code>orignalNumber</code> based on the <code>refNumber</code>.
     * <p>
     * This is used for formatting the simulation metric value which shows at the end of the training along with the uncertainty value.
     * <p>
     * For detailed explanation of the calculations involved, please visit <a href="https://github.com/SkymindIO/pathmind-webapp/wiki/Calculations-for-formatting-metrics-with-&-without-uncertainty-value-on-UI">the GitHub wiki page</a>.
     *
     * @param originalNumber  the original number to be formatted.
     * @param refNumber       the ref number with its designated number of significant figures which doesn't require further formatting.
     * @param refNumberSigFig is used for determining the actual sig. fig. of figures ending with 0 at its last position.
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
        int originalNumberNonDecimalDigits = getNumberOfNonDecimalDigits(originalNumberPrecision, originalNumberScale);
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

    /**
     * convert Python style double value to Java Double style
     * @param str
     * @return
     */
    public static double convertValidDouble(String str) {
        str = str.trim();
        if (str.equalsIgnoreCase("nan")) {
            str =  String.valueOf(Double.NaN);
        } else if (str.equalsIgnoreCase("inf")) {
            str = String.valueOf(Double.POSITIVE_INFINITY);
        } else if (str.equalsIgnoreCase("-inf")) {
            str =  String.valueOf(Double.NEGATIVE_INFINITY);
        }
        return Double.valueOf(str);
    }

    public static String addThousandsSeparatorToNumber(String numberText) {
        double number = Double.parseDouble(numberText);
        if (number < 1000) {
            return numberText;
        }
        return NumberFormat.getInstance().format(number);
    }

    public static String removeThousandsSeparatorFromNumber(String numberText) {
        return numberText.replace(",", "");
    }
}