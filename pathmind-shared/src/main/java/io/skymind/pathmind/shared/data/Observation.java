package io.skymind.pathmind.shared.data;

import io.skymind.pathmind.shared.constants.ObservationDataType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Observation {
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
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Observation observation = (Observation) o;
        // If there is no id in either then use the instance comparison.
        return id == observation.id;
    }

    public ObservationDataType getDataTypeEnum() {
        return ObservationDataType.getEnumFromValue(dataType).orElse(null);
    }
    
    public void setDataTypeEnum(ObservationDataType dataTypeEnum) {
        dataType = dataTypeEnum.getValue();
    }

}
