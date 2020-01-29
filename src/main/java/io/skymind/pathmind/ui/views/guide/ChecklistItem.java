package io.skymind.pathmind.ui.views.guide;

public class ChecklistItem {
	private String name;
	private String path;
	private String state;
	private String htmlId;
	private long id;

	public ChecklistItem(String name, String path, String state, String htmlId) {
		this.name = name;
		this.path = path;
		this.state = state;
		this.htmlId = htmlId;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getState() {
		return state;
	}

	public String getHtmlId() {
		return htmlId;
	}

	public long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setHtmlId(String htmlId) {
		this.htmlId = htmlId;
	}

	public void setId(long id) {
		this.id = id;
	}
}