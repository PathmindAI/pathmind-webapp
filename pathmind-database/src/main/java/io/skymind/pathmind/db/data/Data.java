package io.skymind.pathmind.db.data;

import java.util.Objects;

public abstract class Data
{
	private long id = -1;
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Data data = (Data) o;
		// If there is no id in either then use the instance comparison.
		return id > -1 && data.id > -1 ? id == data.id : this == o;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
