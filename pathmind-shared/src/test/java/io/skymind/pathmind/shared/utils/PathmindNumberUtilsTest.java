package io.skymind.pathmind.shared.utils;

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
        Assert.assertEquals("2799", PathmindNumberUtils.formatNumber(2799.141));
        Assert.assertEquals("42372", PathmindNumberUtils.formatNumber(42371.95234));
    }

	@Test
    public void testSetSigFigBasedOnAnotherDouble() {
        Assert.assertEquals("1.13", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(1.134, 0.22));
        Assert.assertEquals("145.1", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(145.134, 0.2));
        Assert.assertEquals("0.56", PathmindNumberUtils.setSigFigBasedOnAnotherDouble(0.55823, 0.23));
        Assert.assertEquals("123", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 123, (double) 30));
        Assert.assertEquals("841", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 840.58, (double) 37));
        Assert.assertEquals("1234", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 1234.5, (double) 6));
        Assert.assertEquals("1236", PathmindNumberUtils.setSigFigBasedOnAnotherDouble((double) 1235.5, (double) 6));
    }

	@Test
    public void testFormatToSigFig() {
        Assert.assertEquals("145.1", PathmindNumberUtils.formatToSigFig(145.134, 4));
        Assert.assertEquals("0.56", PathmindNumberUtils.formatToSigFig(0.55823, 2));
        Assert.assertEquals("0.558", PathmindNumberUtils.formatToSigFig(0.55823, 3));
        Assert.assertEquals("18", PathmindNumberUtils.formatToSigFig(18.5, 2));
        Assert.assertEquals("120", PathmindNumberUtils.formatToSigFig(123.25, 2));
        Assert.assertEquals("1000", PathmindNumberUtils.formatToSigFig(1149.33, 1));
        Assert.assertEquals("1100", PathmindNumberUtils.formatToSigFig(1149.33, 2));
        Assert.assertEquals("1150", PathmindNumberUtils.formatToSigFig(1149.33, 3));
    }

}