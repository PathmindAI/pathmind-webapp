package io.skymind.pathmind.services.training.cloud.aws;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class BashScriptCreatorUtilTest {
    @Test
    public void testVar() {
        Assert.assertEquals("export NAME='the value'", BashScriptCreatorUtil.var("NAME", "the value"));
        Assert.assertEquals("export NAME='the '\"'\"'value'\"'\"''", BashScriptCreatorUtil.var("NAME", "the 'value'"));
    }

    @Test
    public void testVarExpr() {
        Assert.assertEquals("export NAME=$(the expr)", BashScriptCreatorUtil.varExp("NAME", "$(the expr)"));
        Assert.assertEquals("export NAME=$('\"'\"'the expr'\"'\"')", BashScriptCreatorUtil.varExp("NAME", "$('the expr')"));
    }

    @Test
    public void testVarCondition() {
        Assert.assertEquals("export NAME=${NAME:='the expr'}", BashScriptCreatorUtil.varCondition("NAME", "the expr"));
        Assert.assertEquals("export NAME=${NAME:='the '\"'\"'expr'\"'\"''}", BashScriptCreatorUtil.varCondition("NAME", "the 'expr'"));
    }

    @Test
    public void createObservationSnippet() {
        List<String> selectedObservations = Arrays.asList("first", "second", "third");
        String expected = "out = new double[3];\n" +
                "out[0] = in.first;\n" +
                "out[1] = in.second;\n" +
                "out[2] = in.third;";
        Assert.assertEquals(expected, BashScriptCreatorUtil.createObservationSnippet(selectedObservations));
    }
}
