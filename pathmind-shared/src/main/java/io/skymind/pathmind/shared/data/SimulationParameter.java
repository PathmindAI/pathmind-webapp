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
    private ParamType type;

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
        switch (type) {
            case STRING:
                return "\"" + value + "\"";
            case BOOLEAN:
                return String.valueOf(Boolean.parseBoolean(value));
            case DATE:
                return String.format("new java.util.Date(%sL)", value);
        }
        return value;
    }

    public boolean isNullString() {
        return this.getType().equals(ParamType.STRING.getValue()) && ParamType.NULL_VALUE.equals(this.getValue());
    }

}
