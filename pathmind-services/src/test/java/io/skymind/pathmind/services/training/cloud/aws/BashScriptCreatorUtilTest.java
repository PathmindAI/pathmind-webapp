package io.skymind.pathmind.services.training.cloud.aws;

import java.util.Arrays;
import java.util.List;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import io.skymind.pathmind.shared.data.Observation;
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
        List<Observation> selectedObservations = Arrays.asList(
                observation("first"),
                observation("second"),
                observation("third")
        );
        String expected = "out = new double[3];\n" +
                "out[0] = in.first;\n" +
                "out[1] = in.second;\n" +
                "out[2] = in.third;";
        Assert.assertEquals(expected, BashScriptCreatorUtil.createObservationSnippet(selectedObservations));
    }

    @Test
    public void createObservationSnippet_withArrayOfDoubles() {
        List<Observation> selectedObservations = Arrays.asList(
                observation("first"),
                observation("second", 2),
                observation("third"),
                observation("fourth", 4)
        );
        String expected = "out = new double[8];\n" +
                "out[0] = in.first;\n" +
                "out[1] = in.second[0];\n" +
                "out[2] = in.second[1];\n" +
                "out[3] = in.third;\n" +
                "out[4] = in.fourth[0];\n"+
                "out[5] = in.fourth[1];\n"+
                "out[6] = in.fourth[2];\n"+
                "out[7] = in.fourth[3];";
        Assert.assertEquals(expected, BashScriptCreatorUtil.createObservationSnippet(selectedObservations));
    }

    private Observation observation(String varName) {
        Observation observation = new Observation();
        observation.setVariable(varName);
        observation.setDataTypeEnum(ObservationDataType.NUMBER);
        return observation;
    }

    private Observation observation(String varName, int arraySize) {
        Observation observation = new Observation();
        observation.setVariable(varName);
        observation.setDataTypeEnum(ObservationDataType.NUMBER_ARRAY);
        observation.setMaxItems(arraySize);
        return observation;
    }
}
