package io.skymind.pathmind.shared.data;

import java.util.Objects;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Observation {
    // this is the name of action masking in observations
    public static final String ACTION_MASKING = "actionMask";

    private long id = -1;
    private long modelId;
    private int arrayIndex;
    private String variable;
    private String dataType;
    private String description;
    private String example;
    private Double min;
    private Double max;
    private Integer minItems;
    private Integer maxItems;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Observation obs = (Observation) o;
        // If there is no id in either then use the instance comparison.
        return id > -1 && obs.id > -1 ? id == obs.id : this == o;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public ObservationDataType getDataTypeEnum() {
        return ObservationDataType.getEnumFromValue(dataType).orElse(null);
    }

    public void setDataTypeEnum(ObservationDataType dataTypeEnum) {
        dataType = dataTypeEnum.getValue();
    }

}
