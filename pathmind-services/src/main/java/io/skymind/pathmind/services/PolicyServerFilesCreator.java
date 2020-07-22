package io.skymind.pathmind.services;

import io.skymind.pathmind.shared.data.Action;
import io.skymind.pathmind.shared.data.Observation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class PolicyServerFilesCreator {

    public String createOutputYaml(List<Action> actions) {
        return IntStream.range(0, actions.size())
                .mapToObj(i -> {
                    Action action = actions.get(i);
                    return String.format("%s: %s", i, action.getName().trim());
                })
                .collect(Collectors.joining("\n"));
    }

    public String createSchemaYaml(List<Observation> observations) {
        YamlObject result = new YamlObject();
        YamlObject observation = result.createObject("Observation");
        observation.putProperty("type", "object");

        Collection<String> requiredPropertiesValues = observations.stream().map(Observation::getVariable).collect(Collectors.toList());
        observation.putProperty("required", requiredPropertiesValues);

        YamlObject properties = observation.createObject("properties");
        observations.forEach(o -> {
            YamlObject property = properties.createObject(o.getVariable());
            property.putProperty("type", o.getDataType());
            switch (o.getDataTypeEnum()) {
                case INTEGER:
                    fillIntegerProperties(property, o);
                    break;
                case NUMBER:
                    fillNumberProperties(property, o);
                    break;
                case NUMBER_ARRAY:
                    fillNumberArrayProperties(property, o);
                    break;
                case INTEGER_ARRAY:
                    fillIntegerArrayProperties(property, o);
                    break;
                default:
            }
            if (StringUtils.isNotEmpty(o.getExample())) {
                property.putProperty("example", o.getExample());
            }
            if (StringUtils.isNotEmpty(o.getDescription())) {
                property.putProperty("description", o.getDescription());
            }
        });

        return result.toString();
    }

    private void fillIntegerProperties(YamlObject property, Observation o) {
        property.putProperty("type", "integer");
        if (o.getMin() != null) {
            property.putProperty("minimum", String.valueOf(o.getMin().intValue()));
        }
        if (o.getMax() != null) {
            property.putProperty("maximum", String.valueOf(o.getMax().intValue()));
        }
    }

    private void fillIntegerArrayProperties(YamlObject property, Observation o) {
        property.putProperty("type", "array");
        if (o.getMinItems() != null) {
            property.putProperty("minItems", o.getMinItems());
        }
        if (o.getMaxItems() != null) {
            property.putProperty("maxItems", o.getMaxItems());
        }
        YamlObject itemsObject = property.createObject("items");
        itemsObject.putProperty("type", "integer");
        fillIntegerProperties(itemsObject, o);
    }

    private void fillNumberArrayProperties(YamlObject property, Observation o) {
        property.putProperty("type", "array");
        if (o.getMinItems() != null) {
            property.putProperty("minItems", o.getMinItems());
        }
        if (o.getMaxItems() != null) {
            property.putProperty("maxItems", o.getMaxItems());
        }
        YamlObject itemsObject = property.createObject("items");
        itemsObject.putProperty("type", "number");
        fillNumberProperties(itemsObject, o);
    }

    private void fillNumberProperties(YamlObject property, Observation o) {
        property.putProperty("format", "double");
        if (o.getMin() != null) {
            property.putProperty("minimum", o.getMin());
        }
        if (o.getMax() != null) {
            property.putProperty("maximum", o.getMax());
        }
    }

    private static class YamlObject {
        private Map<String, Object> data = new LinkedHashMap<>();

        public void putProperty(String key, Object value) {
            data.put(key, value);
        }

        public YamlObject createObject(String property) {
            YamlObject newObject = new YamlObject();
            data.put(property, newObject);
            return newObject;
        }

        private List<String> getRepresentationLines() {
            List<String> result = new ArrayList<>();
            for(Map.Entry<String, Object> entry: data.entrySet()) {
                result.addAll(getRepresentationLines(entry.getKey(), entry.getValue()));
            }
            return result;
        }

        private List<String> getRepresentationLines(String key, Object value) {
            List<String> result = new ArrayList<>();
            if (value instanceof Collection) {
                result.add(String.format("%s:", key));
                result.addAll(((Collection<? extends Object>) value).stream().map(i -> String.format("  - %s", i.toString())).collect(Collectors.toList()));
            }
            else if (value instanceof YamlObject) {
                result.add(String.format("%s:", key));
                result.addAll(((YamlObject)value).getRepresentationLines().stream().map(v -> "  " + v).collect(Collectors.toList()));
            }
            else {
                result.add(String.format("%s: %s", key, value.toString()));
            }
            return result;
        }

        public String toString() {
            return StringUtils.join(getRepresentationLines(), "\n");
        }
    }
}
