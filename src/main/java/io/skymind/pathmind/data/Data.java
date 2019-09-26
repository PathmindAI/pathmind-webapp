package io.skymind.pathmind.data;

public abstract class Data
{
	// TODO -> Paul -> Please hook up the value archived value in the database.
	private long id;
	private String name;
	private boolean archived = false;

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

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
}
