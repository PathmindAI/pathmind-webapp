package io.skymind.pathmind.webapp.ui.components;

public interface PathmindFilterInterface<T>
{
	public boolean isMatch(T t, String searchValue);
}
