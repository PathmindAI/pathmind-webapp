package io.skymind.pathmind.webapp.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SearchBox extends HorizontalLayout
{
	private String CLASS_NAME = "search-box";
	private TextField searchTextField = new TextField();
	private Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));

	public SearchBox()
	{
		setSpacing(false);
		setJustifyContentMode(FlexComponent.JustifyContentMode.END);

		searchTextField.setWidthFull();
		searchTextField.setClearButtonVisible(true);
		searchTextField.setValueChangeMode(ValueChangeMode.ON_CHANGE);
		searchButton.addClickListener(click -> search());
    	searchTextField.addValueChangeListener(change -> search());

		addClassName(CLASS_NAME);
    	searchTextField.addClassName(CLASS_NAME + "_text-field");
    	searchButton.addClassNames(CLASS_NAME + "_button");

		add(searchTextField, searchButton);
	}

	public void search()
	{
	    Notification.show("Searching");
	}

}
