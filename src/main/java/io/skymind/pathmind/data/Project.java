package io.skymind.pathmind.data;

import java.time.LocalDateTime;

public class Project extends Data
{
	private LocalDateTime dateCreated;
	private LocalDateTime lastActivityDate;

	private long pathmindUserId;

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
}

