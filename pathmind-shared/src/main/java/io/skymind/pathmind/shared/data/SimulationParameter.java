package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.ParamType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimulationParameter extends Data implements DeepCloneableInterface<SimulationParameter> {

    private static final long serialVersionUID = 1963529929934242024L;
    private Long modelId;
    private Long experimentId;
    private Integer index;
    private String key;
    private String value;
    private Integer type;

    public SimulationParameter shallowClone() {
        return super.shallowClone(new SimulationParameter(
                modelId,
                experimentId,
                index,
                key,
                value,
                type));
    }

    public String getWrappedValue() {
        if (type == ParamType.STRING.getValue())
            return "\"" + value + "\"";

        if (type == ParamType.BOOLEAN.getValue())
            return String.valueOf(Boolean.parseBoolean(value));

        if (type == ParamType.DATE.getValue()) {
            return String.format("new java.util.Date(%sL)", value);
        }

        return value;
    }
}
