package io.skymind.pathmind.ui.components;

public interface PathmindFilterInterface<T>
{
	public boolean isMatch(T t, String searchValue);
}
