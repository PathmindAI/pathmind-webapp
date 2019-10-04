package io.skymind.pathmind.ui.components;

import java.util.List;

public interface FilterableComponent<T>
{
	public List<T> getData();
	public void setFilteredData(List<T> filteredData);
}
