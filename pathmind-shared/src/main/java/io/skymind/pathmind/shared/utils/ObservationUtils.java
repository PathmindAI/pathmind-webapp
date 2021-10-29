package io.skymind.pathmind.shared.utils;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import io.skymind.pathmind.shared.data.Observation;
import org.apache.commons.collections4.CollectionUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObservationUtils {

    private static final Yaml yaml;

    static {
        DumperOptions yamlOptions = new DumperOptions();
        yamlOptions.setPrettyFlow(true);
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(yamlOptions);
    }

    public static String toYaml(List<Observation> observations) {
        Map<String, List<?>> obss = new LinkedHashMap<>();
        obss.put("observations",
                CollectionUtils.emptyIfNull(observations).stream()
                        .sorted(Comparator.comparing(Observation::getVariable))
                        .map(Observation::getVariable)
                        .collect(Collectors.toList())
        );
        String obsYaml = yaml.dump(obss);
        return obsYaml;
    }

    public static List<Observation> fromYaml(String yamlString) {
        List<Observation> observations = new ArrayList<>();
        LinkedHashMap<String, List<String>> observationsMap = yaml.load(yamlString);
        CollectionUtils.emptyIfNull(observationsMap.get("observations")).forEach(name -> {
            Observation obs = new Observation();
            obs.setVariable(name);
            obs.setDataTypeEnum(ObservationDataType.NUMBER);
            obs.setArrayIndex(observations.size());
            observations.add(obs);
        });
        return observations;
    }

}
