package io.skymind.pathmind.webapp.ui.components;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import io.skymind.pathmind.webapp.ui.views.search.SearchResultsView;

public class SearchBox extends HorizontalLayout
{
    private String searchType = "All";
    private String CLASS_NAME = "search-box";
    private Select searchSelect = typeSelect();
	private TextField searchTextField = new TextField();
	private Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));

	public SearchBox(){
		setSpacing(false);

		searchTextField.setWidthFull();
        searchTextField.setClearButtonVisible(true);
        searchTextField.getElement().setAttribute("theme", "small");
        searchSelect.getElement().setAttribute("theme", "small");
		searchButton.addClickListener(click -> search());
    	searchTextField.addValueChangeListener(change -> search());

		addClassName(CLASS_NAME);
        searchSelect.addClassName(CLASS_NAME + "_select");
    	searchTextField.addClassName(CLASS_NAME + "_text-field");
        searchButton.addClassNames(CLASS_NAME + "_button");

		add(searchSelect, searchTextField, searchButton);
    }

    public Select typeSelect() {
        Select<String> typeSelect = new Select<>();
        typeSelect.addValueChangeListener(event -> searchType = event.getValue());
        typeSelect.setItems("All", "Project", "Model", "Experiment");
        typeSelect.setValue("All");
        return typeSelect;
    }

	public void search(){
	    if (StringUtils.isNotBlank(searchTextField.getValue())) {
            String searchTypeAndText = searchType+":"+searchTextField.getValue();
            String searchTerm = URLEncoder.encode(searchTypeAndText, StandardCharsets.UTF_8);
            getUI().ifPresent(ui -> ui.navigate(SearchResultsView.class, searchTerm));
        }
	}

	public void clearSearchValue() {
	    searchTextField.setValue("");
    }

	public void setValue(String text) {
	    searchTextField.setValue(text);
    }

	public String getValue() {
	    return searchTextField.getValue();
    }
}
