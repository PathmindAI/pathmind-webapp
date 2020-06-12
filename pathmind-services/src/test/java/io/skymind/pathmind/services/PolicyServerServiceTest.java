package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import io.skymind.pathmind.shared.data.Action;
import io.skymind.pathmind.shared.data.Observation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PolicyServerServiceTest {
    private PolicyServerService policyServerService;

    @Before
    public void setup() {
        policyServerService = new PolicyServerService();
    }

    @Test
    public void testOutputYamlCreation() {
        List<Action> actions = Stream.of("the first", " second   ", "Third")
                .map(name -> {
                    Action action = new Action();
                    action.setName(name);
                    return action;
                })
                .collect(Collectors.toList());
        String yaml = policyServerService.createOutputYaml(actions);
        String expected = "0: the first\n" +
                "1: second\n" +
                "2: Third";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationBoolean() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.BOOLEAN);
        observation.setExample("True");
        observation.setDescription("A description.");
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: boolean\n" +
                "      example: True\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationInteger() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.INTEGER);
        observation.setExample("17");
        observation.setDescription("A description.");
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: integer\n" +
                "      example: 17\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationIntegerWithMaxAndMin() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.INTEGER);
        observation.setExample("17");
        observation.setDescription("A description.");
        observation.setMin(3.);
        observation.setMax(7.);
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: integer\n" +
                "      minimum: 3\n" +
                "      maximum: 7\n" +
                "      example: 17\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationNumber() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.NUMBER);
        observation.setExample("17.417");
        observation.setDescription("A description.");
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: number\n" +
                "      format: double\n" +
                "      example: 17.417\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }


    @Test
    public void testSchemaYamlCreationNumberWithMaxAndMin() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.NUMBER);
        observation.setExample("17.417");
        observation.setDescription("A description.");
        observation.setMin(3.);
        observation.setMax(7.);
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: number\n" +
                "      format: double\n" +
                "      minimum: 3.0\n" +
                "      maximum: 7.0\n" +
                "      example: 17.417\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationArrayNumber() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.NUMBER_ARRAY);
        observation.setExample("17.5, 18.443");
        observation.setDescription("A description.");
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: number\n" +
                "        format: double\n" +
                "      example: 17.5, 18.443\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationArrayNumberWithMaxMinMaxLengthAndMinLength() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.NUMBER_ARRAY);
        observation.setExample("17.5, 18.443");
        observation.setDescription("A description.");
        observation.setMin(3.);
        observation.setMax(7.);
        observation.setMinItems(1);
        observation.setMaxItems(10);
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: array\n" +
                "      minItems: 1\n" +
                "      maxItems: 10\n" +
                "      items:\n" +
                "        type: number\n" +
                "        format: double\n" +
                "        minimum: 3.0\n" +
                "        maximum: 7.0\n" +
                "      example: 17.5, 18.443\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationArrayInteger() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.INTEGER_ARRAY);
        observation.setExample("17, 18");
        observation.setDescription("A description.");
        observation.setMin(3.);
        observation.setMax(7.);
        observation.setMinItems(1);
        observation.setMaxItems(10);
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: array\n" +
                "      minItems: 1\n" +
                "      maxItems: 10\n" +
                "      items:\n" +
                "        type: integer\n" +
                "        minimum: 3\n" +
                "        maximum: 7\n" +
                "      example: 17, 18\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlCreationArrayIntegerWithMaxMinMaxLengthAndMinLength() {
        Observation observation = new Observation();
        observation.setVariable("theVariable");
        observation.setDataTypeEnum(ObservationDataType.INTEGER_ARRAY);
        observation.setExample("17, 18");
        observation.setDescription("A description.");
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        type: integer\n" +
                "      example: 17, 18\n" +
                "      description: A description.";
        Assert.assertEquals(expected, yaml);
    }

    @Test
    public void testSchemaYamlMultipleObservations() {
        Observation observation1 = new Observation();
        observation1.setVariable("theVariable");
        observation1.setDataTypeEnum(ObservationDataType.BOOLEAN);
        observation1.setExample("True");
        observation1.setDescription("A description.");

        Observation observation2 = new Observation();
        observation2.setVariable("otherVariable");
        observation2.setDataTypeEnum(ObservationDataType.INTEGER);
        observation2.setExample("17");
        observation2.setDescription("Another description.");
        String yaml = policyServerService.createSchemaYaml(Arrays.asList(observation1, observation2));
        String expected = "Observation:\n" +
                "  type: object\n" +
                "  required:\n" +
                "    - theVariable\n" +
                "    - otherVariable\n" +
                "  properties:\n" +
                "    theVariable:\n" +
                "      type: boolean\n" +
                "      example: True\n" +
                "      description: A description.\n" +
                "    otherVariable:\n" +
                "      type: integer\n" +
                "      example: 17\n" +
                "      description: Another description.";
        Assert.assertEquals(expected, yaml);
    }

}
