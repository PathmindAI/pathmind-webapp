package io.skymind.pathmind.services.training.cloud.aws;

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

}
