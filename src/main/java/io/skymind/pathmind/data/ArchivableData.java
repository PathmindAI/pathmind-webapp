package io.skymind.pathmind.data;

/**
 * This could also be an interface but honestly it would all be the same code repeated so I went with an abstract class.
 */
public class ArchivableData extends Data
{
	// TODO -> Paul -> Please hook up the value archived value in the database.
	private boolean archived = false;

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
}
