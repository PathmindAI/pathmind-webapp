package io.skymind.pathmind.ui.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class SearchBox extends HorizontalLayout
{
	private TextField searchTextField = new TextField();
	private Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));

	public SearchBox()
	{
		setSpacing(false);
		searchTextField.setWidthFull();

		add(searchTextField, searchButton);
	}

	public void addSearchButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		searchButton.addClickListener(listener);
	}

	// TODO -> Do we want to add search as you type?
	public String getSearchValue() {
		return searchTextField.getValue();
	}
}
