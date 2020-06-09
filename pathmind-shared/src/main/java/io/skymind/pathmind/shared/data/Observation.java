package io.skymind.pathmind.shared.data;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Observation {
    private long id = -1;
    private long modelId;
    private String variable;
    private String dataType;
    private String description;
    private String example;
    private Double min;
    private Double max;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Observation observation = (Observation) o;
        // If there is no id in either then use the instance comparison.
        return id == observation.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
