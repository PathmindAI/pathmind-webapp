package io.skymind.pathmind.shared.data;

/**
 * This could also be an interface but honestly it would all be the same code repeated so I went with an abstract class.
 */
public class ArchivableData extends Data
{
    private static final long serialVersionUID = -1337915604505664839L;
	// TODO -> DH -> Please hook up the value archived value in the database.
	private boolean archived = false;

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
}
