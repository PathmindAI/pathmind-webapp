package io.skymind.pathmind.webapp.ui.components;

import java.util.List;

public interface FilterableComponent<T>
{
	public List<T> getData();
	public void setFilteredData(List<T> filteredData);
}
