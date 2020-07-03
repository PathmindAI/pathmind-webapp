package io.skymind.pathmind.webapp.ui.components;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import io.skymind.pathmind.webapp.ui.views.search.SearchResultsView;

public class SearchBox extends HorizontalLayout
{
	private String CLASS_NAME = "search-box";
	private TextField searchTextField = new TextField();
	private Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));

	public SearchBox(){
		setSpacing(false);

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

	public void search(){
	    if (StringUtils.isNotBlank(searchTextField.getValue())) {
            String searchTerm = URLEncoder.encode(searchTextField.getValue(), StandardCharsets.UTF_8);
	        getUI().ifPresent(ui -> ui.navigate(SearchResultsView.class, searchTerm));
	    }
	}

	public void clearSearchValue() {
	    searchTextField.setValue("");
    }
}
