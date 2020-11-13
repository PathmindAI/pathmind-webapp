package io.skymind.pathmind.shared.data;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Data implements Serializable {
    private static final long serialVersionUID = -3430522101432605914L;
    private long id = -1;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Data data = (Data) o;
        // If there is no id in either then use the instance comparison.
        return id > -1 && data.id > -1 ? id == data.id : this == o;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected <T extends ArchivableData> T shallowClone(T data) {
        data.setId(id);
        data.setName(name);
        return data;
    }

    protected <T extends Data> T shallowClone(T data) {
        data.setId(id);
        data.setName(name);
        return data;
    }
}
