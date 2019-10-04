package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBox<T> extends HorizontalLayout
{
	private TextField searchTextField = new TextField();
	private Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));

	private Grid<T> grid;
	private boolean isSelectFirstOnSearch;

	private ArrayList<FilterableComponent> filterableComponents;

	public SearchBox(Grid<T> grid, PathmindFilterInterface searchPredicate) {
		this(grid, searchPredicate, false);
	}

	public SearchBox(Grid<T> grid, PathmindFilterInterface searchPredicate, boolean isSelectFirstOnSearch)
	{
		this.grid = grid;
		this.isSelectFirstOnSearch = isSelectFirstOnSearch;

		this.filterableComponents = new ArrayList<FilterableComponent>();

		setSpacing(false);
		setJustifyContentMode(FlexComponent.JustifyContentMode.END);

		searchTextField.setWidthFull();
		searchTextField.setClearButtonVisible(true);
		searchTextField.setValueChangeMode(ValueChangeMode.EAGER);
		searchButton.addClickListener(click -> search(searchPredicate));
    	searchTextField.addValueChangeListener(change -> search(searchPredicate));

		add(searchTextField, searchButton);
	}

	public void search(PathmindFilterInterface searchPredicate)
	{
		ListDataProvider<T> listDataProvider = ((ListDataProvider<T>)grid.getDataProvider());
		listDataProvider.addFilter(t -> searchPredicate.isMatch(t, searchTextField.getValue()));

		if(isSelectFirstOnSearch)
			grid.select(listDataProvider.getItems().iterator().next());

		filterableComponents.stream().forEach(filterableComponent -> {
			filterableComponent.setFilteredData((List<T>)filterableComponent.getData().stream()
					.filter(t -> searchPredicate.isMatch((T)t, searchTextField.getValue()))
					.collect(Collectors.toList()));
		});
	}

	public void addFilterableComponents(FilterableComponent... newFilterableComponents) {
		this.filterableComponents.addAll(Arrays.asList(newFilterableComponents));
	}
}
