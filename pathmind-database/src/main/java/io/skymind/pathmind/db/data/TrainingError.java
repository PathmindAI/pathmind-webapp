package io.skymind.pathmind.db.data;


public class TrainingError {
	private long id;
	private String keyword;
	private String description;
	private boolean restartable;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public boolean isRestartable() {
		return restartable;
	}

	public void setRestartable(boolean restartable) {
		this.restartable = restartable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
