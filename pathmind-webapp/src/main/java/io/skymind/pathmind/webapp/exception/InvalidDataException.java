package io.skymind.pathmind.webapp.exception;

import io.skymind.pathmind.shared.exception.PathMindException;

public class InvalidDataException extends PathMindException
{
	public InvalidDataException(String message) {
		super(message);
	}
}
