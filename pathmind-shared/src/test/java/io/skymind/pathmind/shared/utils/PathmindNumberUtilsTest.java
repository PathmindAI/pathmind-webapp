package io.skymind.pathmind.shared.utils;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class PathmindNumberUtilsTest {

    @Test
    public void testFormatNumber() {
        Assert.assertEquals("0.000079", PathmindNumberUtils.formatNumber(0.00007858213));
        Assert.assertEquals("0.56", PathmindNumberUtils.formatNumber(0.55823));
        Assert.assertEquals("1.7", PathmindNumberUtils.formatNumber(1.6893235));
        Assert.assertEquals("6.4", PathmindNumberUtils.formatNumber(6.3912));
        Assert.assertEquals("10", PathmindNumberUtils.formatNumber(10.349));
        Assert.assertEquals("11", PathmindNumberUtils.formatNumber(10.6496));
        Assert.assertEquals("197", PathmindNumberUtils.formatNumber(196.78));
        Assert.assertEquals("2,799", PathmindNumberUtils.formatNumber(2799.141));
        Assert.assertEquals("42,372", PathmindNumberUtils.formatNumber(42371.95234));
    }

    @Test
    public void testSetSigFigBasedOnAnotherDoubleFor1SigFig() {
        Assert.assertEquals("1", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1.0, 1.0, 1));
        Assert.assertEquals("1.0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1.0, 0.2, 1));
        Assert.assertEquals("1234", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1234.5, (double) 6, 1));
        Assert.assertEquals("1236", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1235.5, (double) 6, 1));
        Assert.assertEquals("12400", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(12353.5, (double) 600, 1));
    }

    @Test
    public void testSetSigFigBasedOnAnotherDoubleFor2SigFig() {
        Assert.assertEquals("0.0003", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.00032, 0.0075, 2));
        Assert.assertEquals("0.040", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.040, 0.051, 2));
        Assert.assertEquals("0.56", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.55823, 0.23, 2));
        Assert.assertEquals("0.999", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.9992, 0.013, 2));
        Assert.assertEquals("1.00", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.9992, 0.13, 2));
        Assert.assertEquals("1.13", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1.134, 0.22, 2));
        Assert.assertEquals("1.1340", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1.134, 0.0022, 2));
        Assert.assertEquals("5", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 5, (double) 30, 2));
        Assert.assertEquals("50", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 50, (double) 30, 2));
        Assert.assertEquals("50.0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 50, 3.0, 2));
        Assert.assertEquals("123", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 123, (double) 30, 2));
        Assert.assertEquals("145.13", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(145.134, 0.20, 2));
        Assert.assertEquals("840.6", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(840.58, 3.7, 2));
        Assert.assertEquals("841", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(840.58, (double) 37, 2));
        Assert.assertEquals("1234.5", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1234.5, (double) 6.0, 2));
        Assert.assertEquals("1235.5", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1235.5, (double) 6.0, 2));
        Assert.assertEquals("1234", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1234.5, (double) 16, 2));
        Assert.assertEquals("1236", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1235.5, (double) 16, 2));
        Assert.assertEquals("1250", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1254.5, (double) 110, 2));
    }

    @Test
    public void testSetSigFigBasedOnAnotherDoubleFor3SigFig() {
        Assert.assertEquals("1.254", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1.25442, 0.312, 3));
        Assert.assertEquals("1254", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1254.5, (double) 110, 3));
        Assert.assertEquals("12354", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(12353.5, (double) 600, 3));
    }

    @Test
    public void testSetSigFigBasedOnAnotherDoubleWithZeroValue() {
        Assert.assertEquals("0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.0, (double) 0, 1));
        Assert.assertEquals("0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.0, 0.0, 1));
        Assert.assertEquals("0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 0, (double) 0, 1));
        Assert.assertEquals("0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 0, 0.0, 1));
        Assert.assertEquals("0.0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.0, 0.12, 1));
        Assert.assertEquals("0.0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.0, 0.0, 2));
        Assert.assertEquals("0.0", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.0, (double) 0, 2));
        Assert.assertEquals("0.00", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.0, 0.12, 2));
    }

    @Test
    public void testFormatToSigFig() {
        Assert.assertEquals("0", PathmindNumberUtils.formatToSigFig(0.0, 1));
        Assert.assertEquals("145.1", PathmindNumberUtils.formatToSigFig(145.134, 4));
        Assert.assertEquals("0.040", PathmindNumberUtils.formatToSigFig(0.04, 2));
        Assert.assertEquals("0.040", PathmindNumberUtils.formatToSigFig(0.040, 2));
        Assert.assertEquals("0.56", PathmindNumberUtils.formatToSigFig(0.55823, 2));
        Assert.assertEquals("0.558", PathmindNumberUtils.formatToSigFig(0.55823, 3));
        Assert.assertEquals("4.5", PathmindNumberUtils.formatToSigFig(4.52, 2));
        Assert.assertEquals("3.0", PathmindNumberUtils.formatToSigFig(3.046, 2));
        Assert.assertEquals("3.0", PathmindNumberUtils.formatToSigFig(3.0, 2));
        Assert.assertEquals("18", PathmindNumberUtils.formatToSigFig(18.5, 2));
        Assert.assertEquals("120", PathmindNumberUtils.formatToSigFig(123.25, 2));
        Assert.assertEquals("1000", PathmindNumberUtils.formatToSigFig(1149.33, 1));
        Assert.assertEquals("1100", PathmindNumberUtils.formatToSigFig(1149.33, 2));
        Assert.assertEquals("1150", PathmindNumberUtils.formatToSigFig(1149.33, 3));
        Assert.assertEquals("0.0001", PathmindNumberUtils.formatToSigFig(.0001330, 1));
        Assert.assertEquals("5432.1000", PathmindNumberUtils.formatToSigFig(5432.1, 8));
        Assert.assertEquals("0.20", PathmindNumberUtils.formatToSigFig(0.2, 2));

        /* Although mathematically this should not happen, the function should still handle it
         * because this function is also used for formatting the number that's passed between double and String
         * which may result in the loss of the end zero in the significant figures.
         */
        Assert.assertEquals("0.0", PathmindNumberUtils.formatToSigFig((double) 0, 2));
        Assert.assertEquals("2.0", PathmindNumberUtils.formatToSigFig((double) 2, 2));
    }

    @Test
    public void testCalculateVariance() {
        List<Double> list1 = Arrays.asList(1.5, 0.5, (double) 1);
        Assert.assertEquals(0.25, PathmindNumberUtils.calculateVariance(list1), 0.0001);

        List<Double> list2 = Arrays.asList(1.5);
        Assert.assertEquals(0, PathmindNumberUtils.calculateVariance(list2), 0.0001);

        List<Double> list3 = Arrays.asList(12.34, 12.34, 12.34);
        Assert.assertEquals(0, PathmindNumberUtils.calculateVariance(list3), 0.0001);

        List<Double> list4 = Arrays.asList(12.34, 10.578, 11.560);
        Assert.assertEquals(0.7795613333, PathmindNumberUtils.calculateVariance(list4), 0.0001);

        List<Double> list5 = Arrays.asList();
        Assert.assertEquals(0, PathmindNumberUtils.calculateVariance(list5), 0.0001);
    }

    @Test
    public void testAddThousandsSeparatorToNumber() {
        Assert.assertEquals("0.000079", PathmindNumberUtils.addThousandsSeparatorToNumber("0.000079"));
        Assert.assertEquals("0.56", PathmindNumberUtils.addThousandsSeparatorToNumber("0.56"));
        Assert.assertEquals("282", PathmindNumberUtils.addThousandsSeparatorToNumber("282"));
        Assert.assertEquals("2,290,000", PathmindNumberUtils.addThousandsSeparatorToNumber("2290000"));
        Assert.assertEquals("880,000", PathmindNumberUtils.addThousandsSeparatorToNumber("880000"));
        Assert.assertEquals("1,040", PathmindNumberUtils.addThousandsSeparatorToNumber("1040"));
        Assert.assertEquals("150,000", PathmindNumberUtils.addThousandsSeparatorToNumber("150000"));
        Assert.assertEquals("46,000", PathmindNumberUtils.addThousandsSeparatorToNumber("46000"));
    }

}