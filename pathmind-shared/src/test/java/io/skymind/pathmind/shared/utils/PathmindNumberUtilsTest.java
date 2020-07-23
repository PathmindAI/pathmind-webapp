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

}