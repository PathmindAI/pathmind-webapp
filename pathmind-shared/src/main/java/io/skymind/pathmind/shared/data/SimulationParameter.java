package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SimulationParameter extends Data implements DeepCloneableInterface<SimulationParameter> {

    private static final long serialVersionUID = 1963529929934242024L;
    private long modelId;
    private int arrayIndex;
    private String dataType;

    public SimulationParameter(long modelId, String name, int arrayIndex, String dataType) {
        this.modelId = modelId;
        this.arrayIndex = arrayIndex;
        this.dataType = dataType;
        setName(name);
    }

    // Used for the cloning method.
    private SimulationParameter(long modelId, int arrayIndex, String dataType) {
        this.modelId = modelId;
        this.arrayIndex = arrayIndex;
        this.dataType = dataType;
    }

    public SimulationParameter shallowClone() {
        return super.shallowClone(new SimulationParameter(
                modelId,
                arrayIndex,
                dataType));
    }
}
