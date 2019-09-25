package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import io.micrometer.core.instrument.util.StringUtils;

import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

public abstract class SearchBox<T> extends HorizontalLayout
{
	protected abstract boolean isMatch(T t, String searchValue);

	private TextField searchTextField = new TextField();
	private Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));

	private Grid<T> grid;
	private Supplier<List<T>> itemListSupplier;
	private BiPredicate<T, String> isMatch;
	private boolean isSelectFirstOnSearch;

	public SearchBox(Grid<T> grid, Supplier<List<T>> itemListSupplier) {
		this(grid, itemListSupplier, false);
	}

	public SearchBox(Grid<T> grid, Supplier<List<T>> itemListSupplier, boolean isSelectFirstOnSearch)
	{
		this.grid = grid;
		this.itemListSupplier = itemListSupplier;
		this.isSelectFirstOnSearch = isSelectFirstOnSearch;

		setSpacing(false);
		setJustifyContentMode(FlexComponent.JustifyContentMode.END);

		searchTextField.setWidthFull();
		searchTextField.setClearButtonVisible(true);
		searchTextField.setValueChangeMode(ValueChangeMode.EAGER);
		searchButton.addClickListener(click -> search());
    	searchTextField.addValueChangeListener(change -> search());

		add(searchTextField, searchButton);
	}

	public void search() {
		if(StringUtils.isEmpty(searchTextField.getValue())) {
			updateGrid(itemListSupplier.get());
		} else {
			updateGrid(itemListSupplier.get().stream()
					.filter(t -> isMatch(t, searchTextField.getValue()))
					.collect(Collectors.toList()));
		}
	}

	private void updateGrid(List<T> results) {
		grid.setItems(results);
		if(isSelectFirstOnSearch && results.size() > 0)
			grid.select(results.get(0));
	}
}
