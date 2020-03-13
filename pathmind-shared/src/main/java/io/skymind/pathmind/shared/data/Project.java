package io.skymind.pathmind.shared.data;

import java.time.LocalDateTime;
import java.util.List;

public class Project extends ArchivableData
{
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;
	private String userNotes;

	private long pathmindUserId;

	// Helper GUI attributes not stored in the database
	private List<Model> models;

	public Project() {
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(LocalDateTime lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public long getPathmindUserId() {
		return pathmindUserId;
	}

	public void setPathmindUserId(long pathmindUserId) {
		this.pathmindUserId = pathmindUserId;
	}

	public String getUserNotes() {
		return userNotes;
	}

	public void setUserNotes(String userNotes) {
		this.userNotes = userNotes;
	}

	public List<Model> getModels() {
		return models;
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}
}

