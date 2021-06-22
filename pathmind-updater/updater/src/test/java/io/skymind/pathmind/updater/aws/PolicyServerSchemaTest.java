package io.skymind.pathmind.updater.aws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.skymind.pathmind.shared.data.Observation;
import io.skymind.pathmind.shared.services.PolicyServerService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import static io.skymind.pathmind.shared.services.PolicyServerService.PolicyServerSchema.typeOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PolicyServerSchemaTest {

    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES));

    @Test
    public void test() throws IOException {

        Observation oInt = new Observation();
        oInt.setDataType("int");

        Observation oIntList = new Observation();
        oIntList.setDataType("int[]");

        Observation oFloatList = new Observation();
        oFloatList.setDataType("float[]");

        PolicyServerService.PolicyServerSchema schema = PolicyServerService.PolicyServerSchema.builder()
                .parameters(
                        PolicyServerService.PolicyServerSchema.Parameters.builder()
                                .discrete(false)
                                .tuple(false)
                                .apiKey("12345asdfg")
                                .urlPath("policy/" + "id1234")
                                .build()
                )
                .observations(
                        Map.of(
                                "one", typeOf(oInt),
                                "two", typeOf(oIntList),
                                "three", typeOf(oFloatList)
                        )
                )
                .build();

        String yamlString = yamlMapper.writeValueAsString(schema);

        System.out.println(yamlString);

        Yaml yaml = new Yaml();

        String expectedSchema = FileUtils.readFileToString(ResourceUtils.getFile("classpath:schema.yaml"), Charset.defaultCharset());

        Map<Object, Object> expected = yaml.load(expectedSchema);
        Map<Object, Object> actual = yaml.load(yamlString);

        assertThat(actual.entrySet(), equalTo(expected.entrySet()));


    }


}