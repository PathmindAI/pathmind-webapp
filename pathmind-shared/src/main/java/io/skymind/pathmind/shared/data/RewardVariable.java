package io.skymind.pathmind.shared.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RewardVariable extends Data {
    private long modelId;
    private int arrayIndex;

    public RewardVariable(long modelId, String name, int arrayIndex) {
        this.modelId = modelId;
        this.arrayIndex = arrayIndex;
        setName(name);
    }
}
