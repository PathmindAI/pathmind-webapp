package io.skymind.pathmind.webapp.api;

public class Response {

	private final String message;

	public Response(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
